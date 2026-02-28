package com.lilemy.aiwebsitebuilder.ai;

import com.lilemy.aiwebsitebuilder.ai.model.HtmlCodeResult;
import com.lilemy.aiwebsitebuilder.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lilemy
 * @date 2026-02-28 23:33
 */
@Slf4j
@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("做一个图库网站，不超过 20 行");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode("做一个留言板网站，不超过 50 行");
        Assertions.assertNotNull(result);
    }
}