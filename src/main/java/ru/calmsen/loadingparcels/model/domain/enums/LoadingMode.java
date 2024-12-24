package ru.calmsen.loadingparcels.model.domain.enums;

public enum LoadingMode {
    /**
     * Погрузка по одной посылке.
     */
    ONEBOX,
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
        try{
            return valueOf(value.toUpperCase());
        }
        catch(Exception e){
            return LoadingMode.EFFICIENT;
        }
    }
}
