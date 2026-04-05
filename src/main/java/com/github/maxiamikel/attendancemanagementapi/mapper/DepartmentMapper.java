package com.github.maxiamikel.attendancemanagementapi.mapper;

import org.springframework.stereotype.Component;

import com.github.maxiamikel.attendancemanagementapi.dto.response.DepartmentResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.Department;

@Component
public class DepartmentMapper {

    public DepartmentResponse toResponse(Department entity) {
        return DepartmentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
