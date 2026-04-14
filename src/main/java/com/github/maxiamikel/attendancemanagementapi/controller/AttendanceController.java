package com.github.maxiamikel.attendancemanagementapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketDetailsResponse;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.TicketMapper;
import com.github.maxiamikel.attendancemanagementapi.security.CustomUserDetails;
import com.github.maxiamikel.attendancemanagementapi.services.AttendanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final TicketMapper ticketMapper;

    @PostMapping("/next")
    public ResponseEntity<ApiResponse<TicketDetailsResponse>> callNext(
            @AuthenticationPrincipal CustomUserDetails user) {

        var nextTicket = attendanceService.callNextTicket(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(ticketMapper.toDetailsResponse(nextTicket)));
    }

    @PostMapping("/next-by-priority")
    public ResponseEntity<ApiResponse<TicketDetailsResponse>> callNextTicketByPriority(
            @RequestParam TicketPriority priority, @AuthenticationPrincipal CustomUserDetails user) {

        var nextTicket = attendanceService.callNextTicketByPriority(priority, user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(ticketMapper.toDetailsResponse(nextTicket)));
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<TicketDetailsResponse>> startTicket(
            @AuthenticationPrincipal CustomUserDetails user) {

        var nextTicket = attendanceService.startTicket(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(ticketMapper.toDetailsResponse(nextTicket)));
    }

}
