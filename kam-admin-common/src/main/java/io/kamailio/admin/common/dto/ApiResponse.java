package io.kamailio.admin.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "统一 API 响应")
public class ApiResponse<T> {

    @Schema(description = "状态码，0 表示成功")
    private final int code;

    @Schema(description = "消息")
    private final String message;

    @Schema(description = "数据")
    private final T data;

    @Schema(description = "时间戳")
    private final String timestamp;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toString();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Success");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(0, message, data);
    }

    public static ApiResponse<Void> success() {
        return success(null, "Success");
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(message, -1);
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        return new ApiResponse<>(code, message, null);
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getTimestamp() { return timestamp; }
}
