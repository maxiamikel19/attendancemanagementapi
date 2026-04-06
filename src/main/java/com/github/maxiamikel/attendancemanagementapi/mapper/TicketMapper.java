package com.github.maxiamikel.attendancemanagementapi.mapper;

import org.springframework.stereotype.Component;

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
}
