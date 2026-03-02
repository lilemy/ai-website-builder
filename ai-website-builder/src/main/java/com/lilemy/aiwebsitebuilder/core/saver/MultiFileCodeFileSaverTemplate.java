package com.lilemy.aiwebsitebuilder.core.saver;

import com.lilemy.aiwebsitebuilder.ai.model.MultiFileCodeResult;
import com.lilemy.aiwebsitebuilder.common.ResultCode;
import com.lilemy.aiwebsitebuilder.exception.ThrowUtils;
import com.lilemy.aiwebsitebuilder.model.enums.CodeGenTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 多文件代码保存器
 *
 * @author lilemy
 * @date 2026-03-02 23:59
 */
public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {

    @Override
    public CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        // 保存 HTML 文件
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        // 保存 CSS 文件
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        // 保存 JavaScript 文件
        writeToFile(baseDirPath, "script.js", result.getJsCode());
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        // 至少要有 HTML 代码，CSS 和 JS 可以为空
        ThrowUtils.throwIf(StringUtils.isBlank(result.getHtmlCode()), ResultCode.SYSTEM_ERROR, "HTML 代码内容不能为空");
    }
}

