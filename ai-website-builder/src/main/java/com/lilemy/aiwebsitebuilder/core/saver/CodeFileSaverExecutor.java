package com.lilemy.aiwebsitebuilder.core.saver;

import com.lilemy.aiwebsitebuilder.ai.model.HtmlCodeResult;
import com.lilemy.aiwebsitebuilder.ai.model.MultiFileCodeResult;
import com.lilemy.aiwebsitebuilder.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 代码文件保存器执行器 根据代码生成类型执行相应的保持逻辑
 *
 * @author lilemy
 * @date 2026-03-03 00:00
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFIleSaverTemplate HTML_CODE_FILE_SAVER_TEMPLATE = new HtmlCodeFIleSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate MULTI_FILE_CODE_FILE_SAVER_TEMPLATE = new MultiFileCodeFileSaverTemplate();

    /**
     * 执行代码保存逻辑
     *
     * @param codeResult  代码结果对象
     * @param codeGenType 代码生成类型
     * @param appId       应用ID
     * @return 保存后的文件目录
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType, Long appId) {
        return switch (codeGenType) {
            case HTML -> HTML_CODE_FILE_SAVER_TEMPLATE.saveCode((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> MULTI_FILE_CODE_FILE_SAVER_TEMPLATE.saveCode((MultiFileCodeResult) codeResult, appId);
        };
    }
}
