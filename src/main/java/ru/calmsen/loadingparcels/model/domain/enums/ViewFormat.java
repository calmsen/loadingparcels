package ru.calmsen.loadingparcels.model.domain.enums;

public enum ViewFormat {
    TXT,
    JSON;

    public static ViewFormat fromString(String value) {
        try{
            return valueOf(value.toUpperCase());
        }
        catch(Exception e){
            return TXT;
        }
    }
}
