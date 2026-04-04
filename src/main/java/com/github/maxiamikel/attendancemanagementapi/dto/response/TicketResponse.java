package com.github.maxiamikel.attendancemanagementapi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketResponse {

    private String passCode;
    private String department;
    private String personalId;
    private String priority;
}
