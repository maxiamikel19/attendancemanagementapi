package com.github.maxiamikel.attendancemanagementapi.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String className, String atributeName) {
        super("No " + className + " found by: " + atributeName);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
