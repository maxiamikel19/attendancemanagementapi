package com.github.maxiamikel.attendancemanagementapi.services;

import java.util.List;
import java.util.UUID;

import com.github.maxiamikel.attendancemanagementapi.dto.request.TicketTransferRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;

public interface AttendanceService {

    Ticket callNextTicket(UUID userId);

    Ticket callNextTicketByPriority(TicketPriority priority, UUID userId);

    Ticket cancelTicket();

    Ticket startTicket();

    Ticket completeTicket();

    Ticket recallTicket();

    Ticket getCurrentTicketTicket();

    Ticket transferTicket(TicketTransferRequest request);

    List<Ticket> getAllWaitingAttendencesForDepartment();
}
