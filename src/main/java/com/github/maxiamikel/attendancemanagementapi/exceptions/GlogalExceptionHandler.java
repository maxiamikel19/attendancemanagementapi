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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.builder()
                .error(ErrorCode.VALIDATION_ERROR.name())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(System.currentTimeMillis())
                .message(errors)
                .build());
    }

    @ExceptionHandler(AlreadyActiveException.class)
    public ResponseEntity<ApiError> handleActiveTicketAlreadyExistsException(AlreadyActiveException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiError.builder()
                .error(ErrorCode.DUPLICATED_ERROR.name())
                .status(HttpStatus.CONFLICT.value())
                .timestamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(CredentialException.class)
    public ResponseEntity<ApiError> handleCredentialException(CredentialException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiError.builder()
                .error(ErrorCode.UNAUTHORIZED_ERROR.name())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiError.builder()
                .error(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiError.builder()
                .error(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .build());
    }
}
