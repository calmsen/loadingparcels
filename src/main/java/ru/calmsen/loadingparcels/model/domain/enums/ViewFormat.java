package ru.calmsen.loadingparcels.model.domain.enums;

/**
 * Формат вывода данных
 */
public enum ViewFormat {
    TXT,
    JSON,
    CSV;

    /**
     * Преобразует строковое значение в перечисление. Не зависит от регистра. В случае ошибки возвращается TXT.
     *
     * @param value строковое представление
     * @return перечисление
     */
    public static ViewFormat fromString(String value) {
        return valueOf(value.toUpperCase());
    }

    /**
     * Переопределяет перечисление, если расширение файла соответствует перечислению.
     *
     * @param outFileName наименование файла
     * @param format текущий формат
     * @return переопределенный формат
     */
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
