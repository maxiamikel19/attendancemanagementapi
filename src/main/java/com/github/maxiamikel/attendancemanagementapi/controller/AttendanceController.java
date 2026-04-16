package com.github.maxiamikel.attendancemanagementapi.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketDetailsResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;
import com.github.maxiamikel.attendancemanagementapi.exceptions.CredentialException;
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
                var nextTicket = attendanceService.callNextTicket(getUserId(user));

                return ok(nextTicket);
        }

        @PostMapping("/next-by-priority")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> callNextTicketByPriority(
                        @RequestParam TicketPriority priority, @AuthenticationPrincipal CustomUserDetails user) {
                var nextTicket = attendanceService.callNextTicketByPriority(priority, getUserId(user));

                return ok(nextTicket);
        }

        @PostMapping("/recall")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> recallTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var waitted = attendanceService.recallTicket(getUserId(user));

                return ok(waitted);
        }

        @PostMapping("/start")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> startTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var called = attendanceService.startTicket(getUserId(user));

                return ok(called);
        }

        @PostMapping("/finalize")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> finalizeTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var attended = attendanceService.finalizeTicket(getUserId(user));

                return ok(attended);
        }

        @GetMapping("/current")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> getCurrentTicketTicket(
                        @AuthenticationPrincipal CustomUserDetails user) {
                var attended = attendanceService.getCurrentTicketTicket(getUserId(user));

                return ok(attended);
        }

        @PatchMapping("/transfer/{departmentId}")
        public ResponseEntity<ApiResponse<TicketDetailsResponse>> transferTicket(
                        @AuthenticationPrincipal CustomUserDetails user, @PathVariable UUID departmentId) {
                var attended = attendanceService.transferTicket(departmentId, getUserId(user));

                return ok(attended);
        }

        @GetMapping("/tickets")
        public ResponseEntity<ApiResponse<List<TicketDetailsResponse>>> getAllWaitingAttendencesForDepartment(
                        @AuthenticationPrincipal CustomUserDetails user) {

                var tickets = attendanceService.getTicketsByStatus(getUserId(user));

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(ticketMapper.toDetailsList(tickets)));
        }

        private UUID getUserId(CustomUserDetails user) {
                if (user == null) {
                        throw new CredentialException("User not authenticated");
                }
                return user.getId();
        }

        private ResponseEntity<ApiResponse<TicketDetailsResponse>> ok(Ticket ticket) {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ApiResponseFactory.success(toResponse(ticket)));
        }

        private TicketDetailsResponse toResponse(Ticket ticket) {
                return ticketMapper.toDetailsResponse(ticket);
        }

}
