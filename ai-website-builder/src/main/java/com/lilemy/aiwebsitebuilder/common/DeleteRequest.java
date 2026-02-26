package com.lilemy.aiwebsitebuilder.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 删除请求
 *
 * @author lilemy
 * @date 2026-02-26 19:00
 */
@Data
public class DeleteRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2717125690511329411L;

    /**
     * id
     */
    private Long id;
}
