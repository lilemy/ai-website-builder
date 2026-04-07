package com.lilemy.aiwebsitebuilder.ai.config;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 自定义 Redis 聊天记录存储
 *
 * @author lilemy
 * @since 2026-04-07 10:56
 */
@Component
public class CustomRedisChatMemoryStore implements ChatMemoryStore {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "chat:memory:";

    /**
     * 设置聊天记录过期时间（秒）
     */
    private static final Long EXPIRATION_SECONDS = 3600L;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String key = KEY_PREFIX + memoryId;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        // 使用 LangChain4j 自带的反序列化工具
        return ChatMessageDeserializer.messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String key = KEY_PREFIX + memoryId;
        // 使用 LangChain4j 自带的序列化工具
        String json = ChatMessageSerializer.messagesToJson(messages);
        // 存为普通的 Redis String，并设置过期时间
        stringRedisTemplate.opsForValue().set(key, json, EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String key = KEY_PREFIX + memoryId;
        stringRedisTemplate.delete(key);
    }
}
