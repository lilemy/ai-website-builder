package com.lilemy.aiwebsitebuilder.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * HTML 代码结果
 *
 * @author lilemy
 * @date 2026-02-28 23:45
 */
@Data
@Description("生成 HTML 代码文件的结果")
public class HtmlCodeResult {

    /**
     * HTML 代码
     */
    @Description("HTML 代码")
    private String htmlCode;

    /**
     * 描述
     */
    @Description("生成代码的描述")
    private String description;
}
