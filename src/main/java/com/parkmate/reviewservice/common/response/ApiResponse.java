package com.parkmate.reviewservice.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean isSuccess;
    private int code;
    private String message;
    private T data;

    private ApiResponse(boolean isSuccess, int code, String message, T data) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static <T> ApiResponse<T> success(ResponseStatus status, T data) {
        return success(status.getCode(), status.getMessage(), data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(ResponseStatus.SUCCESS, data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    public static <T> ApiResponse<T> error(ResponseStatus status) {
        return error(status.getCode(), status.getMessage());
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus.is2xxSuccessful(), httpStatus.value(), message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(httpStatus.is2xxSuccessful(), httpStatus.value(), message, null);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return new ApiResponse<>(httpStatus.is2xxSuccessful(), httpStatus.value(), httpStatus.getReasonPhrase(), data);
    }
}