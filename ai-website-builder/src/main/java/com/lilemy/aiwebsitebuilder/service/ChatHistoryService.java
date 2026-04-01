package com.lilemy.aiwebsitebuilder.service;

import com.lilemy.aiwebsitebuilder.model.dto.chathistory.ChatHistoryQueryRequest;
import com.lilemy.aiwebsitebuilder.model.entity.ChatHistory;
import com.lilemy.aiwebsitebuilder.model.vo.chathistory.ChatHistoryVO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author lilemy
 * @since 2026-04-01 15:07
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 创建对话历史
     *
     * @param appId       应用id
     * @param message     消息
     * @param messageType 消息类型
     * @param userId      用户id
     * @return 是否创建成功
     */
    Boolean createChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * 根据应用 id 删除对话历史
     *
     * @param appId 应用 id
     * @return 是否删除成功
     */
    Boolean deleteByAppId(Long appId);

    /**
     * 获取对话历史
     *
     * @param appId          应用 id
     * @param pageSize       每页大小
     * @param lastCreateTime 最后创建时间
     * @return 对话历史
     */
    Page<ChatHistoryVO> getChatHistoryVOPage(Long appId, int pageSize, LocalDateTime lastCreateTime);

    /**
     * 获取查询条件
     *
     * @param request 对话历史查询请求
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest request);
}
