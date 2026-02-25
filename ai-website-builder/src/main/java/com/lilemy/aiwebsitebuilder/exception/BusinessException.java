package com.lilemy.aiwebsitebuilder.exception;

import com.lilemy.aiwebsitebuilder.common.ResultCode;
import lombok.Getter;

import java.io.Serial;

/**
 * 自定义业务异常
 *
 * @author lilemy
 * @date 2026-02-25 16:04
 */
@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5415855680606089061L;

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(ResultCode code) {
        super(code.getMessage());
        this.code = code.getCode();
    }

    public BusinessException(ResultCode code, String message) {
        super(message);
        this.code = code.getCode();
    }
}
