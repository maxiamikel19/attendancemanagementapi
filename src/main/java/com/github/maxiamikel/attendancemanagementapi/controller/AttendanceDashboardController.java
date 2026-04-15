package com.github.maxiamikel.attendancemanagementapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maxiamikel.attendancemanagementapi.dto.response.ApiResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.AttendanceDashboardResponse;
import com.github.maxiamikel.attendancemanagementapi.mapper.ApiResponseFactory;
import com.github.maxiamikel.attendancemanagementapi.services.AttendanceDashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/dashboards")
@RequiredArgsConstructor
public class AttendanceDashboardController {

    private final AttendanceDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<AttendanceDashboardResponse>> getDashboard() {

        var dashboard = dashboardService.getDashboard();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseFactory.success(dashboard));
    }

}
