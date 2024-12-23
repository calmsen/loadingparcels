package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.List;

public interface LoadingAlgorithm {
    LoadingMode getMode();

    List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight, int trucksCount);
}
