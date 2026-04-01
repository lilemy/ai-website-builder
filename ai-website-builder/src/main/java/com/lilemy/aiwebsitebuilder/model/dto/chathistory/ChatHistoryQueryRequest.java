package com.lilemy.aiwebsitebuilder.model.dto.chathistory;

import com.lilemy.aiwebsitebuilder.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对话记录查询请求
 *
 * @author lilemy
 * @since 2026-04-01 15:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 506381213145331210L;

    /**
     * 消息
     */
    private String message;

    /**
     * 消息类型(user/ai)
     */
    private String messageType;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 游标查询 - 最后一条记录的创建时间
     * 用于分页查询，获取早于此时间的记录
     */
    private LocalDateTime lastCreateTime;
}
