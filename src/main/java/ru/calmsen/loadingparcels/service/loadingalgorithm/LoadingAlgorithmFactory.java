package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

public class LoadingAlgorithmFactory {
    public LoadingAlgorithm Create(LoadingMode mode) {
        switch (mode) {
            case ONEBOX -> {
                return new OneBoxLoadingAlgorithm();
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
