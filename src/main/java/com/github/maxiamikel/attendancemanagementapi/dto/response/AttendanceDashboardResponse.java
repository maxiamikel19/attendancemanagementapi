package com.github.maxiamikel.attendancemanagementapi.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttendanceDashboardResponse {

    private List<TicketDetailsResponse> waiting;
    private List<TicketDetailsResponse> recentCalled;
    private TicketDetailsResponse current;
}
