package com.github.maxiamikel.attendancemanagementapi.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketDetailsResponse;
import com.github.maxiamikel.attendancemanagementapi.dto.response.TicketResponse;
import com.github.maxiamikel.attendancemanagementapi.entity.Ticket;
import com.github.maxiamikel.attendancemanagementapi.utils.ApiUtils;

@Component
public class TicketMapper {

    public TicketResponse toResponse(Ticket entity) {

        String passCode = formatPassCode(entity.getPassCode());
        return TicketResponse
                .builder()
                .department(entity.getDepartment().getName())
                .passCode(passCode)
                .personalId(getMaskPersonalId(entity.getPersonalId()))
                .priority(entity.getPriority().name())
                .build();
    }

    private String formatPassCode(String passCode) {
        String[] parts = passCode.split("-");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid passCode format");
        }

        return String.format("%s-%s", parts[0], parts[2]);
    }

    private String getMaskPersonalId(String personalId) {

        return ApiUtils.maskPersonalId(personalId);
    }

    public TicketDetailsResponse toDetailsResponse(Ticket entity) {
        String passCode = formatPassCode(entity.getPassCode());
        return TicketDetailsResponse
                .builder()
                .id(entity.getId())
                .box(entity.getBox() != null ? entity.getBox().getBoxNumber() : "")
                .priority(entity.getPriority().name())
                .ticketStatus(entity.getTicketStatus().name())
                .lastUpdate(entity.getLastUpdate())
                .department(entity.getDepartment().getName())
                .passCode(passCode)
                .personalId(getMaskPersonalId(entity.getPersonalId()))
                .priority(entity.getPriority().name())
                .recallCount(entity.getRecallCount())
                .createdAt(entity.getCreatedAt())
                .lastUpdate(entity.getLastUpdate())
                .build();
    }

    public List<TicketResponse> toResponseList(List<Ticket> tickets) {
        return tickets
                .stream()
                .map(ticket -> toResponse(ticket))
                .toList();
    }

    public List<TicketDetailsResponse> toDetailsList(List<Ticket> tickets) {
        return tickets
                .stream()
                .map(ticket -> toDetailsResponse(ticket))
                .toList();
    }
}
