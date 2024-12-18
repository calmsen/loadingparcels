package calmsen.loadingalgorithm;

import calmsen.model.domain.Box;
import calmsen.model.domain.Truck;
import calmsen.model.domain.enums.LoadingMode;

import java.util.List;

public abstract class LoadingAlgorithm {
    public abstract LoadingMode getMode();

    public abstract List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight);
}
