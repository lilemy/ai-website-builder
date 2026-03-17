package com.lilemy.aiwebsitebuilder.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.bean.BeanUtil;
import com.lilemy.aiwebsitebuilder.annotation.RepeatSubmit;
import com.lilemy.aiwebsitebuilder.common.BaseResponse;
import com.lilemy.aiwebsitebuilder.common.DeleteRequest;
import com.lilemy.aiwebsitebuilder.common.ResultCode;
import com.lilemy.aiwebsitebuilder.common.ResultUtils;
import com.lilemy.aiwebsitebuilder.constant.UserConstant;
import com.lilemy.aiwebsitebuilder.exception.ThrowUtils;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppAdminUpdateRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppCreateRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppQueryRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppUpdateRequest;
import com.lilemy.aiwebsitebuilder.model.entity.App;
import com.lilemy.aiwebsitebuilder.model.vo.app.AppVO;
import com.lilemy.aiwebsitebuilder.service.AppService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 应用 控制层。
 *
 * @author lilemy
 * @since 2026-03-04 22:57
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    @Operation(summary = "创建应用")
    @RepeatSubmit()
    @PostMapping("/create")
    public BaseResponse<Long> createApp(@RequestBody AppCreateRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(appService.createApp(request));
    }

    @Operation(summary = "更新应用")
    @RepeatSubmit()
    @PutMapping("/update")
    public BaseResponse<Boolean> updateApp(@RequestBody AppUpdateRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(appService.updateApp(request));
    }

    @Operation(summary = "删除应用")
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteApp(@RequestBody DeleteRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(appService.deleteApp(request));
    }

    @Operation(summary = "根据 id 获取应用详情")
    @GetMapping("/get/vo")
    public BaseResponse<AppVO> getAppVOById(Long id) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(appService.getAppVO(id));
    }

    @Operation(summary = "获取登录用户应用列表")
    @GetMapping("/my/list/vo")
    public BaseResponse<Page<AppVO>> getLoginUserAppVOPage(AppQueryRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(appService.getLoginUserAppVOPage(request));
    }

    @Operation(summary = "获取精选应用列表")
    @GetMapping("/choiceness/list/vo")
    public BaseResponse<Page<AppVO>> getChoicenessAppVOPage(AppQueryRequest request) {
        ThrowUtils.throwIf(request == null, ResultCode.PARAMS_ERROR);
        return ResultUtils.success(appService.getChoicenessAppVOPage(request));
    }

    @Operation(summary = "管理员删除应用")
    @PostMapping("/admin/delete")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteAppByAdmin(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ResultCode.PARAMS_ERROR);
        long id = deleteRequest.getId();
        // 判断是否存在
        App oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ResultCode.NOT_FOUND_ERROR);
        boolean result = appService.removeById(id);
        return ResultUtils.success(result);
    }

    @Operation(summary = "管理员更新应用")
    @RepeatSubmit()
    @PostMapping("/admin/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateAppByAdmin(@RequestBody AppAdminUpdateRequest appAdminUpdateRequest) {
        ThrowUtils.throwIf(appAdminUpdateRequest == null || appAdminUpdateRequest.getId() == null, ResultCode.PARAMS_ERROR);
        long id = appAdminUpdateRequest.getId();
        // 判断是否存在
        App oldApp = appService.getById(id);
        ThrowUtils.throwIf(oldApp == null, ResultCode.NOT_FOUND_ERROR);
        App app = new App();
        BeanUtil.copyProperties(appAdminUpdateRequest, app);
        boolean result = appService.updateById(app);
        ThrowUtils.throwIf(!result, ResultCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @Operation(summary = "管理员根据 id 获取应用详情")
    @GetMapping("/admin/get/vo")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<AppVO> getAppVOByIdByAdmin(long id) {
        ThrowUtils.throwIf(id <= 0, ResultCode.PARAMS_ERROR);
        // 获取封装类
        return ResultUtils.success(appService.getAppVO(id));
    }

    @Operation(summary = "管理员分页获取应用列表")
    @PostMapping("/admin/list/vo")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<AppVO>> getAppVOByPageByAdmin(@RequestBody AppQueryRequest appQueryRequest) {
        ThrowUtils.throwIf(appQueryRequest == null, ResultCode.PARAMS_ERROR);
        long pageNum = appQueryRequest.getPageNum();
        long pageSize = appQueryRequest.getPageSize();
        QueryWrapper queryWrapper = appService.getQueryWrapper(appQueryRequest);
        Page<App> appPage = appService.page(Page.of(pageNum, pageSize), queryWrapper);
        // 数据封装
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, appPage.getTotalRow());
        appVOPage.setRecords(appService.getAppVOList(appPage.getRecords()));
        return ResultUtils.success(appVOPage);
    }
}
