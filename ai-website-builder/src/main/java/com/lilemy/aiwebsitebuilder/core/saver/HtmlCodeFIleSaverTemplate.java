package com.lilemy.aiwebsitebuilder.core.saver;

import com.lilemy.aiwebsitebuilder.ai.model.HtmlCodeResult;
import com.lilemy.aiwebsitebuilder.common.ResultCode;
import com.lilemy.aiwebsitebuilder.exception.ThrowUtils;
import com.lilemy.aiwebsitebuilder.model.enums.CodeGenTypeEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * HTML 单文件代码保存器
 *
 * @author lilemy
 * @date 2026-03-02 23:56
 */
public class HtmlCodeFIleSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        // 保存文件
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML 代码不能为空
        ThrowUtils.throwIf(StringUtils.isBlank(result.getHtmlCode()), ResultCode.SYSTEM_ERROR, "HTML 代码内容不能为空");
    }
}
