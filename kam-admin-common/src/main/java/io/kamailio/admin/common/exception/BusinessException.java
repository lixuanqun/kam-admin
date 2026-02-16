package io.kamailio.admin.common.exception;

/**
 * 业务异常，可由 server 的 GlobalExceptionHandler 统一转换为 ApiResponse。
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        this(message, -1);
    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
