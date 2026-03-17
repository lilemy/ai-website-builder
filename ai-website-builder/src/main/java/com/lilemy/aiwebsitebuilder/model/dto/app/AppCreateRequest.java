package com.lilemy.aiwebsitebuilder.model.dto.app;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 应用创建请求
 *
 * @author lilemy
 * @date 2026-03-04 22:58
 */
@Data
public class AppCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6186614639332651200L;

    /**
     * 应用初始化 prompt
     */
    private String initPrompt;
}
