package com.github.maxiamikel.attendancemanagementapi.dto.request;

import org.hibernate.validator.constraints.br.CPF;

import com.github.maxiamikel.attendancemanagementapi.enums.TicketPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TicketRequest {

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Personal ID is required")
    @CPF(message = "Invalid personal ID")
    private String personalId;

    @NotNull(message = "Priority is required")
    private TicketPriority priority;
}
