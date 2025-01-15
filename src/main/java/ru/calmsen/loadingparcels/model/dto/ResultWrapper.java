package ru.calmsen.loadingparcels.model.dto;

public record ResultWrapper(String data, String error) {
    public boolean hasError() {
        return error != null;
    }
}
