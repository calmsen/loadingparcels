package ru.calmsen.billing.model.domain.enums;

/**
 * Формат вывода данных
 */
public enum ViewFormat {
    TXT,
    JSON,
    CSV;

    /**
     * Преобразует строковое значение в перечисление. Не зависит от регистра.
     *
     * @param value строковое представление
     * @return перечисление
     */
    public static ViewFormat fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
