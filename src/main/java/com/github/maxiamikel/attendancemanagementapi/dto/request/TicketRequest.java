package com.github.maxiamikel.attendancemanagementapi.dto.request;

import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TicketRequest {

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Personal ID is required")
    private String personalId;

    @NotNull(message = "Priority is required")
    private TicketPriority priority;
}
