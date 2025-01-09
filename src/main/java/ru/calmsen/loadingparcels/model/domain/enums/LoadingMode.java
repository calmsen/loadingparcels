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

    public static LoadingMode fromString(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            return LoadingMode.ONEPARCEL;
        }
    }
}
