package com.github.maxiamikel.attendancemanagementapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.request.TicketRequest;
import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketResponse;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.TicketMapper;
import com.github.maxiamikel.attendancemanagementapi.services.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> generateTicket(@Valid @RequestBody TicketRequest request) {
        var ticket = ticketService.generateTicket(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseFactory.created(ticketMapper.toResponse(ticket)));
    }

}
