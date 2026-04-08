package com.lilemy.aiwebsitebuilder.service;

/**
 * 截图服务
 *
 * @author lilemy
 * @since 2026-04-08 23:16
 */
public interface ScreenshotService {

    /**
     * 生成并上传截图
     *
     * @param webUrl 需截图的网页地址
     * @return 可访问的地址
     */
    String generateAndUploadScreenshot(String webUrl);
}
