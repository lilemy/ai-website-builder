package com.lilemy.aiwebsitebuilder.config;

import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置
 *
 * @author lilemy
 * @date 2026-02-25 16:26
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfig {

    private String host;

    private Integer port;

    private Integer database;

    private String password;

    private Long ttl;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(database)
                .setPassword(password);
        return Redisson.create(config);
    }

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore() {
        return RedisChatMemoryStore.builder()
                .prefix("chat:memory:")
                .host(host)
                .port(port)
                .user("default")
                .password(password)
                .ttl(ttl)
                .build();
    }
}
