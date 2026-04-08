package com.lilemy.aiwebsitebuilder.service;

import com.lilemy.aiwebsitebuilder.common.DeleteRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppCreateRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppDeployRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppQueryRequest;
import com.lilemy.aiwebsitebuilder.model.dto.app.AppUpdateRequest;
import com.lilemy.aiwebsitebuilder.model.entity.App;
import com.lilemy.aiwebsitebuilder.model.vo.app.AppVO;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author lilemy
 * @since 2026-03-04 22:57
 */
public interface AppService extends IService<App> {

    /**
     * 应用聊天生成代码
     *
     * @param appId   应用id
     * @param message 聊天内容
     * @return 生成的代码
     */
    Flux<String> chatToGenCode(Long appId, String message);

    /**
     * 应用部署
     *
     * @param request 应用部署请求
     * @return 部署后的访问链接
     */
    String deployApp(AppDeployRequest request);

    /**
     * 异步生成应用截图
     *
     * @param appId  应用id
     * @param appUrl 应用访问链接
     */
    void generateAppScreenshotAsync(Long appId, String appUrl);

    /**
     * 创建应用
     *
     * @param request 应用创建请求
     * @return 应用id
     */
    Long createApp(AppCreateRequest request);

    /**
     * 更新应用
     *
     * @param request 应用更新请求
     * @return 是否更新成功
     */
    Boolean updateApp(AppUpdateRequest request);

    /**
     * 删除应用
     *
     * @param request 应用删除请求
     * @return 是否删除成功
     */
    Boolean deleteApp(DeleteRequest request);

    /**
     * 获取当前用户应用分页列表
     *
     * @param request 应用查询请求
     * @return 应用分页列表
     */
    Page<AppVO> getLoginUserAppVOPage(AppQueryRequest request);

    /**
     * 获取精选应用分页列表
     *
     * @param request 应用查询请求
     * @return 应用分页列表
     */
    Page<AppVO> getChoicenessAppVOPage(AppQueryRequest request);

    /**
     * 获取应用视图对象
     *
     * @param id 应用id
     * @return 应用视图对象
     */
    AppVO getAppVO(Long id);

    /**
     * 获取应用视图对象
     *
     * @param app 应用
     * @return 应用视图对象
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用视图对象列表
     *
     * @param appList 应用列表
     * @return 应用视图对象列表
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 获取查询条件
     *
     * @param request 应用查询请求
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(AppQueryRequest request);
}
