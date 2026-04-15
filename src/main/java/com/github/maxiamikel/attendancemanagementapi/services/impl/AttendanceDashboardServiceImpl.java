package com.github.maxiamikel.attendancemanagementapi.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.maxiamikel.attendancemanagementapi.dto.response.AttendanceDashboardResponse;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketStatus;
import com.github.maxiamikel.attendancemanagementapi.mapper.TicketMapper;
import com.github.maxiamikel.attendancemanagementapi.repository.TicketRepository;
import com.github.maxiamikel.attendancemanagementapi.services.AttendanceDashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceDashboardServiceImpl implements AttendanceDashboardService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    public AttendanceDashboardResponse getDashboard() {

        List<TicketStatus> statusIn = List.of(TicketStatus.CALLED, TicketStatus.ATTENDING, TicketStatus.CANCELLED,
                TicketStatus.FINALIZED);

        var waiting = ticketRepository
                .findTop10ByTicketStatusOrderByLastUpdateAsc(TicketStatus.WAITING);

        var recentCalled = ticketRepository
                .findTop5ByTicketStatusInOrderByLastUpdateDesc(statusIn);

        var current = ticketRepository
                .findTop1ByTicketStatusOrderByLastUpdateDesc(TicketStatus.CALLED);

        return AttendanceDashboardResponse.builder()
                .current(current.map(ticketMapper::toDetailsResponse).orElse(null))
                .recentCalled(ticketMapper.toDetailsList(recentCalled))
                .waiting(ticketMapper.toDetailsList(waiting))
                .build();
    }

}
