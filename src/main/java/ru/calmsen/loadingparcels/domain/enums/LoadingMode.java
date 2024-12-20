package ru.calmsen.loadingparcels.domain.enums;

public enum LoadingMode {
    SIMPLE,
    EFFICIENT;

    public static LoadingMode fromString(String value) {
        try{
            return valueOf(value.toUpperCase());
        }
        catch(Exception e){
            return null;
        }
    }
}
