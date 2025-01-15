package ru.calmsen.loadingparcels.view;

/**
 * Представление для модели типа InputData
 */
public interface View<InputData> {
    String buildOutputData(InputData data);
}
