package com.lilemy.aiwebsitebuilder.common;

/**
 * @author lilemy
 * @date 2026-02-25 16:08
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 响应
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResultCode.SUCCESS.getCode(), data, ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功
     *
     * @param <T> 数据类型
     * @return 响应
     */
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(ResultCode.SUCCESS.getCode(), null, ResultCode.SUCCESS.getMessage());
    }

    /**
     * 失败
     *
     * @param code 错误状态码
     * @return 响应
     */
    public static <T> BaseResponse<T> error(ResultCode code) {
        return new BaseResponse<>(code);
    }

    /**
     * 失败
     *
     * @param code    错误状态码
     * @param message 错误信息
     * @return 响应
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     *
     * @param code 错误状态码
     * @return 响应
     */
    public static <T> BaseResponse<T> error(ResultCode code, String message) {
        return new BaseResponse<>(code.getCode(), null, message);
    }
}
