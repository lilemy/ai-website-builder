package com.lilemy.aiwebsitebuilder.service.impl;

import cn.hutool.core.io.FileUtil;
import com.lilemy.aiwebsitebuilder.common.ResultCode;
import com.lilemy.aiwebsitebuilder.exception.ThrowUtils;
import com.lilemy.aiwebsitebuilder.manager.rustfs.OssHelper;
import com.lilemy.aiwebsitebuilder.service.ScreenshotService;
import com.lilemy.aiwebsitebuilder.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

/**
 * 截图服务实现类
 *
 * @author lilemy
 * @since 2026-04-08 23:18
 */
@Slf4j
@Service
public class ScreenshotServiceImpl implements ScreenshotService {

    @Resource
    private OssHelper ossHelper;

    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        ThrowUtils.throwIf(StringUtils.isBlank(webUrl), ResultCode.PARAMS_ERROR, "网址不能为空");
        log.info("开始生成截图: {}", webUrl);
        // 生成本地截图
        String localScreenshotPath = WebScreenshotUtils.saveWebPageScreenshot(webUrl);
        ThrowUtils.throwIf(StringUtils.isBlank(localScreenshotPath), ResultCode.OPERATION_ERROR, "本地截图生成失败");
        final String SCREENSHOT_PREFIX = "screenshot";
        try {
            String ossUrl = ossHelper.uploadFile(SCREENSHOT_PREFIX, Paths.get(localScreenshotPath));
            ThrowUtils.throwIf(StringUtils.isBlank(ossUrl), ResultCode.OPERATION_ERROR, "截图上传到对象存储失败");
            log.info("网页截图生成并上传成功: {} -> {}", webUrl, ossUrl);
            return ossUrl;
        } finally {
            // 清理本地文件
            cleanupLocalFile(localScreenshotPath);
        }
    }

    /**
     * 清理本地文件
     *
     * @param localFilePath 本地文件路径
     */
    private void cleanupLocalFile(String localFilePath) {
        File localFile = new File(localFilePath);
        if (localFile.exists()) {
            File parentDir = localFile.getParentFile();
            FileUtil.del(parentDir);
            log.info("本地截图文件已清除: {}", localFilePath);
        }
    }
}
