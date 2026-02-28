package com.lilemy.aiwebsitebuilder.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * 多文件 HTML 代码结果
 *
 * @author lilemy
 * @date 2026-02-28 23:46
 */
@Data
@Description("生成多个代码文件的结果")
public class MultiFileCodeResult {

    /**
     * HTML 代码
     */
    @Description("HTML 代码")
    private String htmlCode;

    /**
     * CSS 样式代码
     */
    @Description("CSS 代码")
    private String cssCode;

    /**
     * JavaScript 代码
     */
    @Description("JavaScript 代码")
    private String jsCode;

    /**
     * 描述
     */
    @Description("生成代码的描述")
    private String description;
}
