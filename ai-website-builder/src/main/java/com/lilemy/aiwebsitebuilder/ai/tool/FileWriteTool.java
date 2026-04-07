package com.lilemy.aiwebsitebuilder.ai.tool;

import com.lilemy.aiwebsitebuilder.constant.AppConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件写入工具类
 *
 * @author lilemy
 * @since 2026-04-07 19:55
 */
@Slf4j
public class FileWriteTool {


    /**
     * 写入文件
     *
     * @param relativeFilePath 文件相对路径
     * @param content          文件内容
     * @param appId            应用 ID
     * @return 文件相对路径
     */
    @Tool("写入文件到指定路径")
    public String writeFile(@P("文件相对路径") String relativeFilePath, @P("要写入文件的内容") String content, @ToolMemoryId Long appId) {
        try {
            Path path = Paths.get(relativeFilePath);
            if (!path.isAbsolute()) {
                // 相对路径处理，创建基于 appId 的项目目录
                String projectDirName = "vue_project_" + appId;
                Path projectRoot = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);
                path = projectRoot.resolve(relativeFilePath);
            }
            // 创建父目录
            Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            // 写入文件内容
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("文件写入成功：{}", path.toAbsolutePath());
            // 返回相对路径
            return "文件写入成功：" + relativeFilePath;
        } catch (IOException e) {
            String errorMsg = "文件写入失败：" + relativeFilePath + "，错误：" + e.getMessage();
            log.error(errorMsg, e);
            return errorMsg;
        }
    }
}
