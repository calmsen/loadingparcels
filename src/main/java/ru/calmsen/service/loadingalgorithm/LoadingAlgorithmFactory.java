package ru.calmsen.service.loadingalgorithm;

import ru.calmsen.model.domain.enums.LoadingMode;

public class LoadingAlgorithmFactory {
    public LoadingAlgorithm Create(LoadingMode mode) {
        switch (mode) {
            case SIMPLE -> {
                return new SimpleLoadingAlgorithm();
            }
            case EFFICIENT -> {
                return new EfficientLoadingAlgorithm();
            }
        }
        throw new IllegalArgumentException("Режим " + mode + "не поддерживается.");
    }
}