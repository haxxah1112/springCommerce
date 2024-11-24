package com.project.common.dto;

import com.project.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    public enum ResponseStatus {
        SUCCESS, FAIL
    }
    private ResponseStatus status;
    private String message;
    private T data;
    private String errorType;


    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseStatus.SUCCESS, "success", data, null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(ResponseStatus.SUCCESS, "success", null, null);
    }

    public static ApiResponse<?> fail(String message, ErrorCode errorType) {
        return new ApiResponse<>(ResponseStatus.FAIL, message, null, errorType.getName());
    }

    public static ApiResponse<Void> fail(String message) {
        return new ApiResponse<>(ResponseStatus.FAIL, message, null, null);
    }
}
