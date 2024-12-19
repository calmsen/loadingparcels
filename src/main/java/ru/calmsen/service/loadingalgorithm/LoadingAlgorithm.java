package ru.calmsen.service.loadingalgorithm;

import ru.calmsen.model.domain.Box;
import ru.calmsen.model.domain.Truck;
import ru.calmsen.model.domain.enums.LoadingMode;

import java.util.List;

public interface LoadingAlgorithm {
    LoadingMode getMode();

    List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight);
}
