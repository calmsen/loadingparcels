package ru.calmsen.loadingparcels.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static LocalDate toDate(String value) {
        return toDate(value, Format.DD_MM_YYYY);
    }

    public static LocalDate toDate(String value, DateUtil.Format dateFormat) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(dateFormat.format));
    }

    public static String toString(LocalDate date) {
        return toString(date, Format.DD_MM_YYYY);
    }

    public static String toString(LocalDate date, DateUtil.Format dateFormat) {
        return date.format(DateTimeFormatter.ofPattern(dateFormat.format));
    }

    public enum Format {
        DD_MM_YYYY("dd.MM.yyyy"),
        ;

        private final String format;

        Format(String format) {
            this.format = format;
        }
    }
}
