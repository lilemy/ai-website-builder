package com.lilemy.aiwebsitebuilder.core.handler;

import com.lilemy.aiwebsitebuilder.model.enums.ChatHistoryMessageTypeEnum;
import com.lilemy.aiwebsitebuilder.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * 简单文本流处理器
 *
 * @author lilemy
 * @since 2026-04-07 21:36
 */
@Slf4j
public class SimpleTextStreamHandler {

    /**
     * 处理文本流 直接收集完整的文本响应
     *
     * @param originFlux         原始文本流
     * @param chatHistoryService 聊天历史服务
     * @param appId              应用 ID
     * @param userId             用户 ID
     * @return 处理后的文本流
     */
    public Flux<String> handle(Flux<String> originFlux, ChatHistoryService chatHistoryService, Long appId, Long userId) {
        StringBuilder aiResponseBuilder = new StringBuilder();
        return originFlux.map(chunk -> {
            // 收集 AI 响应内容
            aiResponseBuilder.append(chunk);
            return chunk;
        }).doOnComplete(() -> {
            // 流式响应完成后，添加 AI 消息到对话历史
            String aiResponse = aiResponseBuilder.toString();
            chatHistoryService.createChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), userId);
        }).doOnError(error -> {
            // 记录错误消息
            String errorMessage = "AI 回复失败：" + error.getMessage();
            chatHistoryService.createChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), userId);
        });
    }
}
