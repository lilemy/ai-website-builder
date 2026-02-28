package com.lilemy.aiwebsitebuilder.core;

import com.lilemy.aiwebsitebuilder.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * @author lilemy
 * @date 2026-03-01 00:20
 */
@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File result = aiCodeGeneratorFacade.generateAndSaveCode("做一个留言板网页，不超过 100行", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(result);
    }
}