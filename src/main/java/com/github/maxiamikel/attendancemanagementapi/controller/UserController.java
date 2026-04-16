package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.UserMapper;
import com.github.maxiamikel.attendancemanagementapi.security.CustomUserDetails;
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
        public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserRequest request) {

                var user = userService.createUser(request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponseFactory.created(userMapper.toResponse(user)));
        }

        @GetMapping("/me")
        public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
                        @AuthenticationPrincipal CustomUserDetails user) {

                return ok(userService.findById(user.getId()));
        }

        @GetMapping("/{userId}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID userId) {

                return ok(userService.findById(userId));
        }

        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<?>> getAll() {

                var users = userService.findAll();
                return ResponseEntity
                                .ok(ApiResponseFactory.success(users.stream().map(userMapper::toResponse).toList()));
        }

        @PutMapping("/{userId}/box/{boxId}")
        public ResponseEntity<ApiResponse<UserResponse>> addBox(@PathVariable UUID userId, @PathVariable UUID boxId) {

                return ok(userService.addBox(userId, boxId));
        }

        @PutMapping("/{userId}/box/{boxId}/update")
        public ResponseEntity<ApiResponse<UserResponse>> updateBox(@PathVariable UUID userId,
                        @PathVariable UUID boxId) {

                return ok(userService.updateBox(userId, boxId));
        }

        @PutMapping("/{userId}/box")
        public ResponseEntity<ApiResponse<UserResponse>> removeBox(@PathVariable("userId") UUID userId) {

                return ok(userService.removeBox(userId));
        }

        @DeleteMapping("/{userId}")
        public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable("userId") UUID userId) {

                userService.deleteUser(userId);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("/{userId}/role/{roleId}")
        public ResponseEntity<ApiResponse<UserResponse>> updateRole(@PathVariable UUID userId,
                        @PathVariable UUID roleId) {

                return ok(userService.updateRole(userId, roleId));
        }

        @PutMapping("/{userId}/department/{departmentId}")
        public ResponseEntity<ApiResponse<UserResponse>> changeDepartment(@PathVariable UUID userId,
                        @PathVariable UUID departmentId) {

                return ok(userService.updateDepartment(userId, departmentId));
        }

        private ResponseEntity<ApiResponse<UserResponse>> ok(User user) {
                return ResponseEntity.ok(
                                ApiResponseFactory.success(toResponse(user)));
        }

        private UserResponse toResponse(User user) {
                return userMapper.toResponse(user);
        }

}
