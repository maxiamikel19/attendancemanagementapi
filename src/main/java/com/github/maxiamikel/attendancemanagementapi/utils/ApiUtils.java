package com.github.maxiamikel.attendancemanagementapi.utils;

public final class ApiUtils {

    private ApiUtils() {
    }

    public static String normalizeStringToUpperCase(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Field cannot be null or empty");
        }
        return value.trim().toUpperCase();
    }
}
