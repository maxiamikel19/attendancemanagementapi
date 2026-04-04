package com.github.maxiamikel.attendancemanagementapi.exceptions;

public class EnumValidationException extends RuntimeException {

    public EnumValidationException(String enumName) {
        super("The " + enumName + " cannot be null");
    }
}
