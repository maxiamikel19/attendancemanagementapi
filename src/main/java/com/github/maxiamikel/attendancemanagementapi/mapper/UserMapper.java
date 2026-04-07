package com.github.maxiamikel.attendancemanagementapi.mapper;

import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.maxiamikel.attendancemanagementapi.dto.response.UserResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .age(getAge(entity.getBirthDate()))
                .box(entity.getBox() != null ? entity.getBox().getBoxNumber() : "")
                .createdAt(entity.getCreatedAt())
                .department(entity.getDepartment().getName())
                .email(entity.getEmail())
                .gender(entity.getGender().name())
                .isActive(entity.isActive() ? "Yes" : "No")
                .isEmailVerified(entity.isEmailVerified() ? "Yes" : "No")
                .role(getRoles(entity))
                .build();
    }

    private int getAge(LocalDate birthDate) {

        if (birthDate == null) {
            throw new IllegalArgumentException("BirthDate cannot be null");
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private String getRoles(User user) {
        return user
                .getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(","));
    }
}
