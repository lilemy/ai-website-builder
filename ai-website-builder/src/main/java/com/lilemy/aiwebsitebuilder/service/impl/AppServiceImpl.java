package com.lilemy.aiwebsitebuilder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.lilemy.aiwebsitebuilder.common.DeleteRequest;
import com.lilemy.aiwebsitebuilder.common.ResultCode;
import com.lilemy.aiwebsitebuilder.constant.AppConstant;
import com.lilemy.aiwebsitebuilder.constant.UserConstant;
import com.lilemy.aiwebsitebuilder.core.AiCodeGeneratorFacade;
import com.lilemy.aiwebsitebuilder.exception.BusinessException;
import com.lilemy.aiwebsitebuilder.exception.ThrowUtils;
import com.lilemy.aiwebsitebuilder.mapper.AppMapper;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppCreateRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppDeployRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppQueryRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppUpdateRequest;
import com.lilemy.aiwebsitebuilder.model.entity.App;
import com.lilemy.aiwebsitebuilder.model.entity.User;
import com.lilemy.aiwebsitebuilder.model.enums.ChatHistoryMessageTypeEnum;
import com.lilemy.aiwebsitebuilder.model.enums.CodeGenTypeEnum;
import com.lilemy.aiwebsitebuilder.model.vo.app.AppVO;
import com.lilemy.aiwebsitebuilder.model.vo.user.UserVO;
import com.lilemy.aiwebsitebuilder.service.AppService;
import com.lilemy.aiwebsitebuilder.service.ChatHistoryService;
import com.lilemy.aiwebsitebuilder.service.UserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author lilemy
 * @since 2026-03-04 22:57
 */
@Slf4j
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Override
    public Flux<String> chatToGenCode(Long appId, String message) {
        // 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ResultCode.PARAMS_ERROR, "应用不存在");
        ThrowUtils.throwIf(StringUtils.isBlank(message), ResultCode.PARAMS_ERROR, "请输入内容");
        // 查询应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ResultCode.NOT_FOUND_ERROR, "应用不存在");
        // 校验用户是否有应用访问权限
        User loginUser = userService.getLoginUser();
        ThrowUtils.throwIf(!app.getUserId().equals(loginUser.getId()), ResultCode.NO_AUTH_ERROR);
        // 获取应用生成类型
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(app.getCodeGenType());
        ThrowUtils.throwIf(codeGenTypeEnum == null, ResultCode.PARAMS_ERROR, "不支持的生成类型");
        // 通过校验后，添加用户消息到对话历史
        chatHistoryService.createChatMessage(appId, message, ChatHistoryMessageTypeEnum.USER.getValue(), loginUser.getId());
        // 调用 AI 生成代码
        Flux<String> contentFlux = aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);
        // 收集 AI 响应内容并在完成后记录到对话历史
        StringBuilder aiResponseBuilder = new StringBuilder();
        return contentFlux.map(chunk -> {
            // 收集 AI 响应内容
            aiResponseBuilder.append(chunk);
            return chunk;
        }).doOnComplete(() -> {
            // 流式响应完成后，添加 AI 消息到对话历史
            String aiResponse = aiResponseBuilder.toString();
            if (StringUtils.isNotBlank(aiResponse)) {
                chatHistoryService.createChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
            }
        }).doOnError(error -> {
            // 记录错误消息
            String errorMessage = "AI 回复失败：" + error.getMessage();
            chatHistoryService.createChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
        });
    }

    @Override
    public String deployApp(AppDeployRequest request) {
        // 参数校验
        ThrowUtils.throwIf(request == null || request.getAppId() == null || request.getAppId() <= 0, ResultCode.PARAMS_ERROR);
        // 查询应用信息
        Long appId = request.getAppId();
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ResultCode.NOT_FOUND_ERROR);
        // 验证用户是否有应用部署权限，仅本人部署
        User loginUser = userService.getLoginUser();
        ThrowUtils.throwIf(!app.getUserId().equals(loginUser.getId()), ResultCode.NO_AUTH_ERROR);
        // 校验是否已经部署
        String deployKey = app.getDeployKey();
        // 没有则生成 8 位随机数（大小写字母 + 数字）
        if (StringUtils.isBlank(deployKey)) {
            deployKey = RandomUtil.randomString(8);
        }
        // 获取代码生成类型，构建源目录路径
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;
        // 检查源目录是否存在
        File sourceDir = new File(sourceDirPath);
        ThrowUtils.throwIf(!sourceDir.exists() || !sourceDir.isDirectory(), ResultCode.OPERATION_ERROR, "应用代码不存在，请先生成代码");
        // 复制文件到部署目录
        String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
        try {
            FileUtil.copyContent(sourceDir, new File(deployDirPath), true);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "部署失败：" + e.getMessage());
        }
        // 更新应用的 deployKey 和部署时间
        App updateApp = new App();
        updateApp.setId(appId);
        updateApp.setDeployKey(deployKey);
        updateApp.setDeployedTime(LocalDateTime.now());
        boolean result = this.updateById(updateApp);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR, "更新应用部署信息失败");
        // 返回可访问的 URL
        return String.format("%s/%s/", AppConstant.CODE_DEPLOY_HOST, deployKey);
    }

    @Override
    public Long createApp(AppCreateRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        // 参数校验
        String initPrompt = request.getInitPrompt();
        ThrowUtils.throwIf(StringUtils.isBlank(initPrompt), ResultCode.PARAMS_ERROR);
        // 获取当前登录用户
        User loginUser = userService.getLoginUser();
        // 构建入库对象
        App app = new App();
        BeanUtils.copyProperties(request, app);
        app.setUserId(loginUser.getId());
        // 应用名称暂时为 initPrompt 前 12 位
        app.setAppName(initPrompt.substring(0, Math.min(12, initPrompt.length())));
        // 暂时设置为多文件生成
        app.setCodeGenType(CodeGenTypeEnum.MULTI_FILE.getValue());
        // 插入数据库
        boolean result = this.save(app);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return app.getId();
    }

    @Override
    public Boolean updateApp(AppUpdateRequest request) {
        ThrowUtils.throwIf(request == null || request.getId() == null || request.getId() <= 0, ResultCode.PARAMS_ERROR);
        // 判断应用是否存在
        Long id = request.getId();
        App oldApp = this.getById(id);
        ThrowUtils.throwIf(oldApp == null, ResultCode.NOT_FOUND_ERROR);
        // 仅本人可以更新
        User loginUser = userService.getLoginUser();
        ThrowUtils.throwIf(!oldApp.getUserId().equals(loginUser.getId()), ResultCode.NO_AUTH_ERROR);
        // 更新数据
        App app = new App();
        app.setId(id);
        app.setAppName(request.getAppName());
        app.setEditTime(LocalDateTime.now());
        boolean result = this.updateById(app);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        // todo 删除应用下的所有文件
        return true;
    }

    @Override
    public Boolean deleteApp(DeleteRequest request) {
        ThrowUtils.throwIf(request == null || request.getId() == null || request.getId() <= 0, ResultCode.PARAMS_ERROR);
        // 判断应用是否存在
        App oldApp = this.getById(request.getId());
        ThrowUtils.throwIf(oldApp == null, ResultCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可以删除
        User loginUser = userService.getLoginUser();
        ThrowUtils.throwIf(!oldApp.getUserId().equals(loginUser.getId()) && UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole()),
                ResultCode.NO_AUTH_ERROR);
        // 删除关联的对话历史
        try {
            Boolean result = chatHistoryService.deleteByAppId(request.getId());
            if (!result) {
                log.error("删除应用下的对话历史失败");
            }
        } catch (Exception e) {
            log.error("删除应用下的对话历史失败：{}", e.getMessage());
        }
        // 删除应用
        return this.removeById(request.getId());
    }

    @Override
    public Page<AppVO> getLoginUserAppVOPage(AppQueryRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        // 限制每页请求数量
        int pageSize = request.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ResultCode.PARAMS_ERROR);
        int pageNum = request.getPageNum();
        // 只查询当前登录用户的应用
        User loginUser = userService.getLoginUser();
        request.setUserId(loginUser.getId());
        QueryWrapper queryWrapper = this.getQueryWrapper(request);
        Page<App> appPage = this.page(Page.of(pageNum, pageSize), queryWrapper);
        // 数据封装
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        appVOPage.setRecords(this.getAppVOList(appPage.getRecords()));
        return appVOPage;
    }

    @Override
    public Page<AppVO> getChoicenessAppVOPage(AppQueryRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        // 限制每页请求数量
        int pageSize = request.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ResultCode.PARAMS_ERROR);
        int pageNum = request.getPageNum();
        // 只查询精选应用
        request.setPriority(AppConstant.CHOICENESS_APP_PRIORITY);
        QueryWrapper queryWrapper = this.getQueryWrapper(request);
        Page<App> appPage = this.page(Page.of(pageNum, pageSize), queryWrapper);
        // 数据封装
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        appVOPage.setRecords(this.getAppVOList(appPage.getRecords()));
        return appVOPage;
    }

    @Override
    public AppVO getAppVO(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        App app = this.getById(id);
        return getAppVO(app);
    }

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        // 关联查询用户信息
        Long userId = app.getUserId();
        if (userId != null) {
            UserVO userVO = userService.getUserVO(userId);
            appVO.setUser(userVO);
        }
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }
        // 批量获取用户信息
        Set<Long> userIds = appList.stream().map(App::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVO));
        return appList.stream().map(app -> {
            AppVO appVO = new AppVO();
            BeanUtils.copyProperties(app, appVO);
            appVO.setUser(userVOMap.get(app.getUserId()));
            return appVO;
        }).toList();
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest request) {
        if (request == null) {
            return QueryWrapper.create();
        }
        String appName = request.getAppName();
        String initPrompt = request.getInitPrompt();
        String codeGenType = request.getCodeGenType();
        Integer priority = request.getPriority();
        Long userId = request.getUserId();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();
        return QueryWrapper.create()
                .like(App::getAppName, appName)
                .like(App::getInitPrompt, initPrompt)
                .eq(App::getCodeGenType, codeGenType)
                .eq(App::getPriority, priority)
                .eq(App::getUserId, userId)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }
}
