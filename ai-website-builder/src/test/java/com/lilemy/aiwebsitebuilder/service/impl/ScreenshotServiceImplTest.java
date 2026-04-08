package com.lilemy.aiwebsitebuilder.service.impl;

import com.lilemy.aiwebsitebuilder.service.ScreenshotService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lilemy
 * @since 2026-04-08 23:31
 */
@Slf4j
@SpringBootTest
class ScreenshotServiceImplTest {

    @Resource
    private ScreenshotService screenshotService;

    @Test
    void generateAndUploadScreenshot() {
        String testUrl = "https://www.lilemy.cn";
        String ossUrl = screenshotService.generateAndUploadScreenshot(testUrl);
        log.info("ossUrl: {}", ossUrl);
        Assertions.assertNotNull(ossUrl);
    }
}