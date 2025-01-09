package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

/**
 * Фабрика создания объекта алгоритма погрузки.
 */
public class LoadingAlgorithmFactory {
    /**
     * Создает объект алгоритма погрузки
     *
     * @param mode тип погрузки
     * @return объект алгоритма погрузки
     */
    public LoadingAlgorithm Create(LoadingMode mode) {
        switch (mode) {
            case ONEPARCEL -> {
                return new OneParcelLoadingAlgorithm();
            }
            case SIMPLE -> {
                return new SimpleLoadingAlgorithm();
            }
            case UNIFORM -> {
                return new UniformLoadingAlgorithm();
            }
            case EFFICIENT -> {
                return new EfficientLoadingAlgorithm();
            }
        }
        throw new IllegalArgumentException("Режим " + mode + " не поддерживается.");
    }
}
