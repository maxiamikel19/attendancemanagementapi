package com.github.maxiamikel.attendancemanagementapi.enums;

import com.github.maxiamikel.attendancemanagementapi.exceptions.EnumValidationException;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender from(String value) {

        String enumName = "gender";

        if (value == null || value.isBlank()) {
            throw new EnumValidationException(enumName);
        }
        return Gender.valueOf(value.trim().toUpperCase());
    }
}
