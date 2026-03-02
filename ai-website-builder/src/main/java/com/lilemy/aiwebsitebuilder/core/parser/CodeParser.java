package com.lilemy.aiwebsitebuilder.core.parser;

/**
 * 代码解析器策略模式
 *
 * @author lilemy
 * @date 2026-03-02 23:43
 */
public interface CodeParser<T> {

    /**
     * 解析代码内容
     *
     * @param codeContent 代码内容
     * @return 解析结果
     */
    T parseCode(String codeContent);
}
