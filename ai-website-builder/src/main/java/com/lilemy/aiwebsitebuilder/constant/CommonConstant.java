package com.lilemy.aiwebsitebuilder.constant;

/**
 * 通用常量
 *
 * @author lilemy
 * @date 2026-02-25 16:30
 */
public interface CommonConstant {

    /**
     * 全局 redis key (业务无关的key)
     */
    String GLOBAL_REDIS_KEY = "global:";

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = GLOBAL_REDIS_KEY + "repeat_submit:";
}
