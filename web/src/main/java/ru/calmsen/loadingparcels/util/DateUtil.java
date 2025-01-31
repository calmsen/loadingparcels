package ru.calmsen.loadingparcels.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static LocalDate toDate(String value) {
        return toDate(value, DD_MM_YYYY);
    }

    public static LocalDate toDate(String value, String dateFormat) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String toString(LocalDate date) {
        return toString(date, DD_MM_YYYY);
    }

    public static String toString(LocalDate date, String dateFormat) {
        return date.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static final String DD_MM_YYYY = "dd.MM.yyyy";
}
