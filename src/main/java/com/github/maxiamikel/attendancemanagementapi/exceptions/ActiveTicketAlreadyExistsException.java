package com.github.maxiamikel.attendancemanagementapi.exceptions;

public class ActiveTicketAlreadyExistsException extends RuntimeException {

    public ActiveTicketAlreadyExistsException(String personalId, String department) {
        super(String.format(
                "User with ID %s already has an active ticket in %s department",
                personalId, department));
    }
}
