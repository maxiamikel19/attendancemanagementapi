package com.github.maxiamikel.attendancemanagementapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    private String name;
}
