package com.github.maxiamikel.attendancemanagementapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.request.UserRequest;
import com.github.maxiamikel.attendancemanagementapi.mapper.UserMapper;
import com.github.maxiamikel.attendancemanagementapi.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody UserRequest request) {

        var user = userService.createAccount(request);
        var response = userMapper.toResponse(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
