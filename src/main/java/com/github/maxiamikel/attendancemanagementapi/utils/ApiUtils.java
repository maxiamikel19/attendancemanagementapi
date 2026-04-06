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

    public static String maskPersonalId(String personalId) {
        if (personalId == null || personalId.isBlank()) {
            return null;
        }

        String value = personalId.trim();

        String[] parts = value.split("-");

        if (parts.length != 2) {
            return "**";
        }

        String numberPart = parts[0];
        String[] blocks = numberPart.split("\\.");

        if (blocks.length < 3) {
            return "**";
        }

        String middle = blocks[1] + "." + blocks[2];

        return "***." + middle + "-**";
    }

}
