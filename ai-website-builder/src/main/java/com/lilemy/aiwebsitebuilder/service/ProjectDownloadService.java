package com.lilemy.aiwebsitebuilder.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 项目下载服务
 *
 * @author lilemy
 * @since 2026-04-09 10:44
 */
public interface ProjectDownloadService {

    /**
     * 下载项目
     *
     * @param projectPath      项目路径
     * @param downloadFileName 下载文件名
     * @param response         响应
     */
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
