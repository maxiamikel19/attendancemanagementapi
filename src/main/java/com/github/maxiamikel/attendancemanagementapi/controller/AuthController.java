package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/activate-account")
    public ResponseEntity<ApiResponse<?>> activateNewAccount(@RequestParam("id") UUID id) {

        authService.activateNewAccount(id);

        Map<String, String> response = new HashMap<>();
        response.put("Success", "Your account has been successfully activated");

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Activation")
                .timestamp(System.currentTimeMillis())
                .data(response)
                .build());
    }
}
