package com.github.maxiamikel.attendancemanagementapi.exceptions;

public class DuplicatedResourceException extends RuntimeException {

    public DuplicatedResourceException(String message) {
        super(message);
    }

    public DuplicatedResourceException(String entityName, String atributeName) {
        super(entityName + " already exist with: " + atributeName);
    }
}
