package com.lilemy.aiwebsitebuilder.ai.model.message;

import dev.langchain4j.service.tool.ToolExecution;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工具执行结果消息
 *
 * @author lilemy
 * @since 2026-04-07 20:59
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ToolExecutedMessage extends StreamMessage {

    /**
     * 工具执行 ID
     */
    private String id;

    /**
     * 工具名称
     */
    private String name;

    /**
     * 输入参数
     *
     */
    private String arguments;

    /**
     * 输出结果
     */
    private String result;

    public ToolExecutedMessage(ToolExecution toolExecution){
        super(StreamMessageTypeEnum.TOOL_EXECUTED.getValue());
        this.id = toolExecution.request().id();
        this.name = toolExecution.request().name();
        this.arguments = toolExecution.request().arguments();
        this.result = toolExecution.result();
    }
}
