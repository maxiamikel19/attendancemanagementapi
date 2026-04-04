package com.github.maxiamikel.attendancemanagementapi.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoxResponse {
    private UUID id;
    private String boxNumber;
    private String status;
}
