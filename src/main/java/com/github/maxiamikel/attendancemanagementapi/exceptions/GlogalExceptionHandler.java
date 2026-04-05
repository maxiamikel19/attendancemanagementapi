package com.github.maxiamikel.attendancemanagementapi.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiError;
import com.github.maxiamikel.attendancemanagementapi.enums.ErrorCode;

@RestControllerAdvice
public class GlogalExceptionHandler {

    @ExceptionHandler(DuplicatedResourceException.class)
    public ResponseEntity<ApiError> handleDuplicatedResourceException(DuplicatedResourceException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiError.builder()
                .error(ErrorCode.DUPLICATED_ERROR.name())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.builder()
                .error(ErrorCode.NOT_FOUND_ERROR.name())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ApiError.builder()
                .error(ErrorCode.VALIDATION_ERROR.name())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .timestamp(System.currentTimeMillis())
                .message(errors)
                .build());
    }
}
