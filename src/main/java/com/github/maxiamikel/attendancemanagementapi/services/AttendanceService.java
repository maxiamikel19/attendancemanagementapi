package com.github.maxiamikel.attendancemanagementapi.services;

import java.util.List;
import java.util.UUID;

import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;

public interface AttendanceService {

    Ticket callNextTicket(UUID userId);

    Ticket callNextTicketByPriority(TicketPriority priority, UUID userId);

    Ticket recallTicket(UUID userId);

    Ticket startTicket(UUID userId);

    Ticket finalizeTicket(UUID userId);

    Ticket getCurrentTicketTicket(UUID userId);

    Ticket transferTicket(UUID departmentId, UUID userId);

    List<Ticket> getAllWaitingAttendencesForDepartment();
}
