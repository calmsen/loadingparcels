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
        return switch (mode) {
            case ONEPARCEL -> new OneParcelLoadingAlgorithm();
            case SIMPLE -> new SimpleLoadingAlgorithm();
            case UNIFORM -> new UniformLoadingAlgorithm();
            case EFFICIENT -> new EfficientLoadingAlgorithm();
        };
    }
}
