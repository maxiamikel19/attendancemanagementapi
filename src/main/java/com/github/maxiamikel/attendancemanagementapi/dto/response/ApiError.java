package com.github.maxiamikel.attendancemanagementapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiError {

    private int status;
    private String error;
    private String message;
    private Long timestamp;
}
