package com.github.maxiamikel.attendancemanagementapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.maxiamikel.attendancemanagementapi.exceptions.EnumValidationException;

public enum TicketPriority {
    NORMAL,
    PRIORITY;

    @JsonCreator
    public static TicketPriority from(String value) {
        if (value == null || value.isBlank()) {
            throw new EnumValidationException("Ticket Priority");
        }
        return TicketPriority.valueOf(value.trim().toUpperCase());
    }
}
