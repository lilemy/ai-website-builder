package com.lilemy.aiwebsitebuilder.ai.model.message;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 流式消息类型枚举
 *
 * @author lilemy
 * @since 2026-04-07 20:59
 */
@Getter
public enum StreamMessageTypeEnum {

    AI_RESPONSE("AI 响应", "ai_response"),
    TOOL_REQUEST("工具请求", "tool_request"),
    TOOL_EXECUTED("工具执行结果", "tool_executed");

    private final String text;
    private final String value;

    StreamMessageTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static StreamMessageTypeEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (StreamMessageTypeEnum anEnum : StreamMessageTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
