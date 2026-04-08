package com.lilemy.aiwebsitebuilder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.aiwebsitebuilder.common.ResultCode;
import com.lilemy.aiwebsitebuilder.constant.UserConstant;
import com.lilemy.aiwebsitebuilder.exception.ThrowUtils;
import com.lilemy.aiwebsitebuilder.mapper.ChatHistoryMapper;
import com.lilemy.aiwebsitebuilder.model.dto.chathistory.ChatHistoryQueryRequest;
import com.lilemy.aiwebsitebuilder.model.entity.App;
import com.lilemy.aiwebsitebuilder.model.entity.ChatHistory;
import com.lilemy.aiwebsitebuilder.model.entity.User;
import com.lilemy.aiwebsitebuilder.model.enums.ChatHistoryMessageTypeEnum;
import com.lilemy.aiwebsitebuilder.model.vo.chathistory.ChatHistoryVO;
import com.lilemy.aiwebsitebuilder.service.AppService;
import com.lilemy.aiwebsitebuilder.service.ChatHistoryService;
import com.lilemy.aiwebsitebuilder.service.UserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话历史 服务层实现。
 *
 * @author lilemy
 * @since 2026-04-01 15:07
 */
@Slf4j
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatHistoryService {

    @Resource
    private UserService userService;

    @Lazy
    @Resource
    private AppService appService;

    @Override
    public Boolean createChatMessage(Long appId, String message, String messageType, Long userId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ResultCode.PARAMS_ERROR, "应用ID不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(message), ResultCode.PARAMS_ERROR, "消息内容不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(messageType), ResultCode.PARAMS_ERROR, "消息类型不能为空");
        ThrowUtils.throwIf(userId == null || userId <= 0, ResultCode.PARAMS_ERROR, "用户ID不能为空");
        // 验证消息类型是否有效
        ChatHistoryMessageTypeEnum messageTypeEnum = ChatHistoryMessageTypeEnum.getEnumByValue(messageType);
        ThrowUtils.throwIf(messageTypeEnum == null, ResultCode.PARAMS_ERROR, "不支持的消息类型: " + messageType);
        ChatHistory chatHistory = ChatHistory.builder()
                .appId(appId)
                .message(message)
                .messageType(messageType)
                .userId(userId)
                .build();
        return this.save(chatHistory);
    }

    @Override
    public Boolean deleteByAppId(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ResultCode.PARAMS_ERROR, "应用 ID不能为空");
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq(ChatHistory::getAppId, appId);
        return this.remove(queryWrapper);
    }

    @Override
    public Page<ChatHistoryVO> getChatHistoryVOPage(Long appId, int pageSize, LocalDateTime lastCreateTime) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ResultCode.PARAMS_ERROR, "应用 ID不能为空");
        ThrowUtils.throwIf(pageSize <= 0, ResultCode.PARAMS_ERROR, "分页大小不能小于 1");
        ThrowUtils.throwIf(pageSize > 50, ResultCode.PARAMS_ERROR, "分页大小不能大于 50");
        // 权限校验
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ResultCode.NOT_FOUND_ERROR, "应用不存在");
        User loginUser = userService.getLoginUser();
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
        boolean isCreator = app.getUserId().equals(loginUser.getId());
        ThrowUtils.throwIf(!isAdmin && !isCreator, ResultCode.NO_AUTH_ERROR, "无权查看该应用的历史记录");
        // 构建查询条件
        ChatHistoryQueryRequest request = new ChatHistoryQueryRequest();
        request.setAppId(appId);
        request.setLastCreateTime(lastCreateTime);
        QueryWrapper queryWrapper = this.getQueryWrapper(request);
        // 查询数据
        Page<ChatHistory> chatHistoryPage = this.page(Page.of(1, pageSize), queryWrapper);
        // 数据封装
        Page<ChatHistoryVO> chatHistoryVOPage = new Page<>(1, pageSize, chatHistoryPage.getTotalRow());
        List<ChatHistory> chatHistoryList = chatHistoryPage.getRecords();
        List<ChatHistoryVO> chatHistoryVOList = BeanUtil.copyToList(chatHistoryList, ChatHistoryVO.class);
        chatHistoryVOPage.setRecords(chatHistoryVOList);
        return chatHistoryVOPage;
    }

    @Override
    public Integer loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount) {
        try {
            // 直接构造查询条件，起始点为 1 而不是 0，用于排除最新的用户消息
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .eq(ChatHistory::getAppId, appId)
                    .orderBy(ChatHistory::getCreateTime, false)
                    .limit(1, maxCount);
            List<ChatHistory> historyList = this.list(queryWrapper);
            if (CollUtil.isEmpty(historyList)) {
                return 0;
            }
            // 反转列表，确保按时间正序（老的在前，新的在后）
            historyList = historyList.reversed();
            // 按时间顺序添加到记忆中
            int loadedCount = 0;
            // 先清理历史缓存，防止重复加载
            chatMemory.clear();
            for (ChatHistory history : historyList) {
                if (ChatHistoryMessageTypeEnum.USER.getValue().equals(history.getMessageType())) {
                    chatMemory.add(UserMessage.from(history.getMessage()));
                    loadedCount++;
                } else if (ChatHistoryMessageTypeEnum.AI.getValue().equals(history.getMessageType())) {
                    chatMemory.add(AiMessage.from(history.getMessage()));
                    loadedCount++;
                }
            }
            log.info("成功为 appId: {} 加载了 {} 条历史对话", appId, loadedCount);
            return loadedCount;
        } catch (Exception e) {
            log.error("加载历史对话失败，appId: {}, error: {}", appId, e.getMessage(), e);
            // 加载失败不影响系统运行，只是没有历史上下文
            return 0;
        }
    }


    @Override
    public QueryWrapper getQueryWrapper(ChatHistoryQueryRequest request) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        if (request == null) {
            return queryWrapper;
        }
        String message = request.getMessage();
        String messageType = request.getMessageType();
        Long appId = request.getAppId();
        Long userId = request.getUserId();
        LocalDateTime lastCreateTime = request.getLastCreateTime();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();
        // 拼接查询条件
        queryWrapper.like(ChatHistory::getMessage, message, StringUtils.isNotBlank(message))
                .eq(ChatHistory::getMessageType, messageType, StringUtils.isNotBlank(messageType))
                .eq(ChatHistory::getAppId, appId, appId != null)
                .eq(ChatHistory::getUserId, userId, userId != null);
        // 游标查询 - 只使用 createTime 作为游标
        if (lastCreateTime != null) {
            queryWrapper.lt(ChatHistory::getCreateTime, lastCreateTime);
        }
        // 排序
        if (StringUtils.isNotBlank(sortField)) {
            queryWrapper.orderBy(sortField, sortOrder.equals("ascend"));
        } else {
            // 默认按照创建时间倒序排序
            queryWrapper.orderBy(ChatHistory::getCreateTime, false);
        }
        return queryWrapper;
    }
}
