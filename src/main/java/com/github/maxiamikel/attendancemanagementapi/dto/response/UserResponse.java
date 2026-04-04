package com.github.maxiamikel.attendancemanagementapi.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private String gender;
    private int age;
    private String isActive;
    private String isEmailVerified;
    private String department;
    private String box;
    private String role;
    private LocalDateTime createdAt;
}
