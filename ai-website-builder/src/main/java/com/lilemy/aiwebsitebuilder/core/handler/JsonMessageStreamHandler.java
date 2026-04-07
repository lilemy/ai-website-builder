package com.lilemy.aiwebsitebuilder.core.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lilemy.aiwebsitebuilder.ai.model.message.*;
import com.lilemy.aiwebsitebuilder.model.enums.ChatHistoryMessageTypeEnum;
import com.lilemy.aiwebsitebuilder.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;

/**
 * Json 消息处理器
 *
 * @author lilemy
 * @since 2026-04-07 21:45
 */
@Slf4j
public class JsonMessageStreamHandler {

    /**
     * 处理 Json 消息流
     *
     * @param originFlux         原始 Json 消息流
     * @param chatHistoryService 聊天历史服务
     * @param appId              应用 ID
     * @param userId             用户 ID
     * @return 处理后的 Json 消息流
     */
    public Flux<String> handle(Flux<String> originFlux, ChatHistoryService chatHistoryService, Long appId, Long userId) {
        // 收集数据用于生成后端记忆格式
        StringBuilder chatHistoryBuilder = new StringBuilder();
        // 用于记录已经见过的工具 ID，判断是否为第一次调用v
        Set<String> seenToolIds = new HashSet<>();
        return originFlux.map(chunk -> {
                    // 解析每个 Json 消息块
                    return handleJsonMessageChunk(chunk, chatHistoryBuilder, seenToolIds);
                })
                .filter(StringUtils::isNotBlank)
                .doOnComplete(() -> {
                    // 流式响应完成后，添加 AI 消息到对话历史
                    String aiResponse = chatHistoryBuilder.toString();
                    chatHistoryService.createChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), userId);
                })
                .doOnError(error -> {
                    // 记录错误消息
                    String errorMessage = "AI 回复失败：" + error.getMessage();
                    chatHistoryService.createChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), userId);
                });
    }

    /**
     * 处理 Json 消息块
     *
     * @param chunk              Json 消息块
     * @param chatHistoryBuilder 用于收集数据用于生成后端记忆格式
     * @param seenToolIds        用于记录已经见过的工具 ID，判断是否为第一次调用
     * @return 处理后的 Json 消息块
     */
    private String handleJsonMessageChunk(String chunk, StringBuilder chatHistoryBuilder, Set<String> seenToolIds) {
        // 解析 Json
        StreamMessage streamMessage = JSONUtil.toBean(chunk, StreamMessage.class);
        StreamMessageTypeEnum typeEnum = StreamMessageTypeEnum.getEnumByValue(streamMessage.getType());
        switch (typeEnum) {
            case AI_RESPONSE -> {
                AiResponseMessage aiMessage = JSONUtil.toBean(chunk, AiResponseMessage.class);
                String data = aiMessage.getData();
                // 直接拼接响应
                chatHistoryBuilder.append(data);
                return data;
            }
            case TOOL_REQUEST -> {
                ToolRequestMessage toolRequestMessage = JSONUtil.toBean(chunk, ToolRequestMessage.class);
                String toolId = toolRequestMessage.getId();
                // 检查是否是第一次看到这个工具 Id
                if (toolId != null && !seenToolIds.contains(toolId)) {
                    // 第一次调用这个工具，记录 Id 并完整返回工具信息
                    seenToolIds.add(toolId);
                    return "\n\n\uD83D\uDD27 [选择工具] 写入文件\n\n";
                } else {
                    // 不是第一次调用这个工具，直接返回空
                    return "";
                }
            }
            case TOOL_EXECUTED -> {
                ToolExecutedMessage toolExecutedMessage = JSONUtil.toBean(chunk, ToolExecutedMessage.class);
                JSONObject jsonObject = JSONUtil.parseObj(toolExecutedMessage.getArguments());
                String relativeFilePath = jsonObject.getStr("relativeFilePath");
                String suffix = FileUtil.getSuffix(relativeFilePath);
                String content = jsonObject.getStr("content");
                String result = String.format("""
                        🛠 [工具调用] 写入文件 %s
                        ```%s
                        %s
                        ```
                        """, relativeFilePath, suffix, content);
                // 输出前端和要持久化的内容
                String output = String.format("\n\n%s\n\n", result);
                chatHistoryBuilder.append(output);
                return output;
            }
            case null -> {
                log.error("消息类型为空");
                return "";
            }
        }
    }
}
