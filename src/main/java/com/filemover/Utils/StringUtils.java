package com.filemover.Utils;

public class StringUtils {

    public static String removeSpecialChars(String line) {
        return line.trim().replaceAll("[^a-zA-Z0-9\\s]+","");
    }

    public static String normalizeSpaces(String line) {
        return line.trim().replaceAll("\\s+", " ");
    }

    public static String pepare(String line) {
        // special character could be followed by few space e.g '  ;-     ', so sequence matters!
        // because
        return StringUtils.normalizeSpaces(StringUtils.removeSpecialChars(line));
    }
}
