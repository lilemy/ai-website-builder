package com.lilemy.aiwebsitebuilder.core.parser;

import com.lilemy.aiwebsitebuilder.ai.model.HtmlCodeResult;
import com.lilemy.aiwebsitebuilder.ai.model.MultiFileCodeResult;
import com.lilemy.aiwebsitebuilder.model.enums.CodeGenTypeEnum;

/**
 * 代码解析执行器
 *
 * @author lilemy
 * @date 2026-03-02 23:52
 */
public class CodeParserExecutor {

    private static final CodeParser<HtmlCodeResult> HTML_CODE_PARSER = new HtmlCodeParser();

    private static final CodeParser<MultiFileCodeResult> MULTI_FILE_CODE_PARSER = new MultiFileCodeParser();

    /**
     * 执行代码解析
     *
     * @param codeContent     代码内容
     * @param codeGenTypeEnum 代码生成类型
     * @return 解析结果
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> HTML_CODE_PARSER.parseCode(codeContent);
            case MULTI_FILE -> MULTI_FILE_CODE_PARSER.parseCode(codeContent);
        };
    }
}
