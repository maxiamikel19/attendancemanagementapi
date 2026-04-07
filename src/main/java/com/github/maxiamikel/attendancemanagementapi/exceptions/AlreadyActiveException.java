package com.github.maxiamikel.attendancemanagementapi.exceptions;

public class AlreadyActiveException extends RuntimeException {

    public AlreadyActiveException(String personalId, String department) {
        super(String.format(
                "User with ID %s already has an active ticket in %s department",
                personalId, department));
    }

    public AlreadyActiveException(String message) {
        super(message);
    }
}
