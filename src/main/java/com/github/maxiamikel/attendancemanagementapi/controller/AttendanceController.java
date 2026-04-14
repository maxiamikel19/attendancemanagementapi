package com.github.maxiamikel.attendancemanagementapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketDetailsResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.User;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.mapper.TicketMapper;
import com.github.maxiamikel.attendancemanagementapi.services.AttendanceService;
import com.github.maxiamikel.attendancemanagementapi.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final TicketMapper ticketMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<TicketDetailsResponse>> callNext() {

        var nextTicket = attendanceService.callNextTicket(getCurrentUser().getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseFactory.success(ticketMapper.toDetailsResponse(nextTicket)));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.findByEmail(email);
    }

}
