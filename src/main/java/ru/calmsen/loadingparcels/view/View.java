package ru.calmsen.loadingparcels.view;

/**
 * Представление для модели типа InputData
 */
public interface View<InputData> {
    String getOutputData(InputData data);
}
