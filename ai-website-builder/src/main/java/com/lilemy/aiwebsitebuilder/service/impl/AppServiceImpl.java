package com.lilemy.aiwebsitebuilder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.lilemy.aiwebsitebuilder.common.DeleteRequest;
import com.lilemy.aiwebsitebuilder.common.ResultCode;
import com.lilemy.aiwebsitebuilder.constant.AppConstant;
import com.lilemy.aiwebsitebuilder.constant.UserConstant;
import com.lilemy.aiwebsitebuilder.exception.ThrowUtils;
import com.lilemy.aiwebsitebuilder.mapper.AppMapper;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppCreateRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppQueryRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppUpdateRequest;
import com.lilemy.aiwebsitebuilder.model.entity.App;
import com.lilemy.aiwebsitebuilder.model.entity.User;
import com.lilemy.aiwebsitebuilder.model.enums.CodeGenTypeEnum;
import com.lilemy.aiwebsitebuilder.model.vo.app.AppVO;
import com.lilemy.aiwebsitebuilder.model.vo.user.UserVO;
import com.lilemy.aiwebsitebuilder.service.AppService;
import com.lilemy.aiwebsitebuilder.service.UserService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

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
