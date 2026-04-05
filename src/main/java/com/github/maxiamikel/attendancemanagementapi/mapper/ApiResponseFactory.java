package com.github.maxiamikel.attendancemanagementapi.mapper;

import org.springframework.http.HttpStatus;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;

public final class ApiResponseFactory {

    private ApiResponseFactory() {
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .timestamp(System.currentTimeMillis())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.CREATED.value())
                .message("Successfully created")
                .timestamp(System.currentTimeMillis())
                .data(data)
                .build();
    }

}
