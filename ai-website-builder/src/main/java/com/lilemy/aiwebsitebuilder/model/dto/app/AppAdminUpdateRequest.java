package com.lilemy.aiwebsitebuilder.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员更新应用请求
 *
 * @author lilemy
 * @date 2026-03-17 22:04
 */
@Data
public class AppAdminUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3116664987522060023L;

    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用封面
     */
    private String cover;

    /**
     * 优先级
     */
    private Integer priority;
}
