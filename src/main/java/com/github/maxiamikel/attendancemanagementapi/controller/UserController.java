package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.request.UserRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.UserResponse;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
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

        @PostMapping
        public ResponseEntity<ApiResponse<UserResponse>> createAccount(@Valid @RequestBody UserRequest request) {

                var user = userService.createAccount(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponseFactory.created(userMapper.toResponse(user)));
        }

        @PutMapping("/{userId}/box/{boxId}")
        public ResponseEntity<ApiResponse<UserResponse>> assignBox(@PathVariable UUID userId,
                        @PathVariable UUID boxId) {

                var user = userService.assignBox(userId, boxId);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(userMapper.toResponse(user)));
        }

        @PutMapping("/{userId}/box/{boxId}/change")
        public ResponseEntity<ApiResponse<UserResponse>> changeBox(@PathVariable UUID userId,
                        @PathVariable UUID boxId) {

                var user = userService.changeBox(userId, boxId);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(userMapper.toResponse(user)));
        }

        @PutMapping("/{userId}/box")
        public ResponseEntity<ApiResponse<UserResponse>> removeBox(@PathVariable("userId") UUID userId) {

                var user = userService.removeBox(userId);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(userMapper.toResponse(user)));
        }

        @DeleteMapping("/{userId}")
        public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable("userId") UUID userId) {

                userService.deleteUser(userId);

                return ResponseEntity.noContent().build();
        }

        @PutMapping("/{userId}/role/{roleId}")
        public ResponseEntity<ApiResponse<UserResponse>> changeRole(@PathVariable UUID userId,
                        @PathVariable UUID roleId) {

                var user = userService.changeRole(userId, roleId);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(userMapper.toResponse(user)));
        }

        @PutMapping("/{userId}/department/{departmentId}")
        public ResponseEntity<ApiResponse<UserResponse>> changeDepartment(@PathVariable UUID userId,
                        @PathVariable UUID departmentId) {

                var user = userService.changeDepartment(userId, departmentId);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(userMapper.toResponse(user)));
        }

        @GetMapping("/me")
        public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(userMapper.toResponse(
                                                userService.findByEmail(authentication.getName()))));
        }
}
