package com.lilemy.aiwebsitebuilder.model.dto.app;

import com.lilemy.aiwebsitebuilder.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 应用查询请求
 *
 * @author lilemy
 * @date 2026-03-17 09:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7818872252893492790L;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用初始化的 prompt
     */
    private String initPrompt;

    /**
     * 代码生成类型（枚举）
     */
    private String codeGenType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 创建用户id
     */
    private Long userId;
}
