package ru.calmsen.billing.model.domain.enums;

public enum Period {
    NONE,
    LAST_MONTH;

    /**
     * Преобразует строковое значение в перечисление. Не зависит от регистра.
     *
     * @param value строковое представление
     * @return перечисление
     */
    public static Period fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
