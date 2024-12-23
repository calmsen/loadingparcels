package ru.calmsen.loadingparcels.model.domain.enums;

public enum LoadingMode {
    ONEBOX,
    SIMPLE,
    UNIFORM,
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
