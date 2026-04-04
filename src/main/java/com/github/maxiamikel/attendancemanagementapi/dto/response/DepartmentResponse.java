package com.github.maxiamikel.attendancemanagementapi.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DepartmentResponse {

    private UUID id;
    private String name;
    private LocalDateTime createdAt;
}
