package calmsen.loadingalgorithm;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.Truck;
import calmsen.model.domain.enums.LoadingMode;

import java.util.List;

public abstract class LoadingAlgorithm {
    public abstract LoadingMode getMode();

    public abstract List<Truck> loadParcels(List<Parcel> parcels, int truckWidth, int truckHeight);
}
