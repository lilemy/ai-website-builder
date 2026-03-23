package com.lilemy.aiwebsitebuilder.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 应用部署请求
 *
 * @author lilemy
 * @date 2026-03-23 15:56
 */
@Data
public class AppDeployRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4665749176121730788L;

    /**
     * 应用ID
     */
    private Long appId;
}
