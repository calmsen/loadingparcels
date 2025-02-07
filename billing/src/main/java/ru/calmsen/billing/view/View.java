package ru.calmsen.billing.view;

/**
 * Представление для модели типа InputData
 */
public interface View<InputData> {
    String buildOutputData(InputData data);
}
