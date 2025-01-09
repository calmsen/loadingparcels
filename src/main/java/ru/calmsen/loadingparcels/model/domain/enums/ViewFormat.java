package ru.calmsen.loadingparcels.model.domain.enums;

/**
 * Формат вывода данных
 */
public enum ViewFormat {
    TXT,
    JSON,
    CSV;

    public static ViewFormat fromString(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            return TXT;
        }
    }

    public static ViewFormat redefineFormat(String outFileName, ViewFormat format) {
        if (outFileName == null) {
            return format;
        }

        if (outFileName.toLowerCase().endsWith("txt")){
            return ViewFormat.TXT;
        }

        if (outFileName.toLowerCase().endsWith("json")){
            return ViewFormat.JSON;
        }

        return outFileName.toLowerCase().endsWith("csv")
                ? ViewFormat.CSV
                : format;
    }
}
