package com.github.maxiamikel.attendancemanagementapi.services;

import com.github.maxiamikel.attendancemanagementapi.dto.request.TicketRequest;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;

public interface TicketService {

    Ticket generateTicket(TicketRequest request);

}
