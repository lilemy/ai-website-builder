package com.lilemy.aiwebsitebuilder.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 对话历史消息类型枚举
 *
 * @author lilemy
 * @since 2026-04-01 15:13
 */
@Getter
public enum ChatHistoryMessageTypeEnum {

    USER("用户", "user"),

    AI("AI", "ai");

    private final String text;

    private final String value;

    ChatHistoryMessageTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值
     * @return 枚举
     */
    public static ChatHistoryMessageTypeEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (ChatHistoryMessageTypeEnum valueEnum : ChatHistoryMessageTypeEnum.values()) {
            if (valueEnum.value.equals(value)) {
                return valueEnum;
            }
        }
        return null;
    }
}
