package com.github.maxiamikel.attendancemanagementapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoxRequest {

    @NotBlank(message = "Box number is required")
    private String boxNumber;
}
