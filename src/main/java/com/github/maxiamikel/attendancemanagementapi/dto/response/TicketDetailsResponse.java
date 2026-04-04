package com.github.maxiamikel.attendancemanagementapi.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDetailsResponse {

    private UUID id;
    private String passCode;
    private String box;
    private String department;
    private String personalId;
    private String ticketStatus;
    private String priority;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
}
