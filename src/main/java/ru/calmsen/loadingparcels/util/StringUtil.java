package ru.calmsen.loadingparcels.util;

public class StringUtil {
    public static String toNullIfEmpty(String str) {
        if (str == null) {
            return null;
        }

        return str.isBlank() ? null : str;
    }
}
