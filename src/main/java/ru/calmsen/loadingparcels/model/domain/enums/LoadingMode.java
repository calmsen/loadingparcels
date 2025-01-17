package ru.calmsen.loadingparcels.model.domain.enums;

/**
 * Тип погрузки
 */
public enum LoadingMode {
    /**
     * Погрузка по одной посылке.
     */
    ONEPARCEL,
    /**
     * Простая погрузка посылок.
     */
    SIMPLE,
    /**
     * Равномерная погрузка посылок.
     */
    UNIFORM,
    /**
     * Максимально плотная погрузка.
     */
    EFFICIENT;

    /**
     * Преобразует строковое значение в перечисление. Не зависит от регистра. В случае ошибки возвращается ONEPARCEL.
     *
     * @param value строковое представление
     * @return перечисление
     */
    public static LoadingMode fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
