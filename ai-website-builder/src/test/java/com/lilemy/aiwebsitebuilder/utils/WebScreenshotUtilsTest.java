package com.lilemy.aiwebsitebuilder.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lilemy
 * @since 2026-04-08 22:28
 */
@Slf4j
@SpringBootTest
class WebScreenshotUtilsTest {

    @Test
    void saveWebPageScreenshot() {
        String testUrl = "https://www.lilemy.cn";
        String webPageScreenshot = WebScreenshotUtils.saveWebPageScreenshot(testUrl);
        log.info("webPageScreenshot: {}", webPageScreenshot);
        Assertions.assertNotNull(webPageScreenshot);
    }
}