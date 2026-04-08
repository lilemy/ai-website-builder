package com.lilemy.aiwebsitebuilder.manager.rustfs;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;

/**
 * @author lilemy
 * @since 2026-04-08 23:07
 */
@Slf4j
@SpringBootTest
class OssHelperTest {

    @Resource
    private OssHelper ossHelper;

    @Test
    void uploadFile() {
        String testFile = "tmp/screenshots/9bab3861/72453_compressed.jpg";
        // 上传 (确保项目根目录下有 hello.txt)
        String test = ossHelper.uploadFile("test", Paths.get(testFile));
        System.out.println(test);
    }
}