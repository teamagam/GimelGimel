package com.teamagam.gimelgimel.app.utils;

/**
 * Utilities for input validations
 */
public class InputValidationUtils {

    public static void notNull(Object obj, String argDescriber) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format("%s is null", argDescriber));
        }
    }

    public static void stringNotNullOrEmpty(String s, String argDescriber) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s is null or empty", argDescriber));
        }
    }
}
