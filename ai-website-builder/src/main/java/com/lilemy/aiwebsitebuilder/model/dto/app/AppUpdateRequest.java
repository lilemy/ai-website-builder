package com.lilemy.aiwebsitebuilder.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 应用更新请求
 *
 * @author lilemy
 * @date 2026-03-17 09:05
 */
@Data
public class AppUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4282251315006744505L;

    /**
     * 应用id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;
}
