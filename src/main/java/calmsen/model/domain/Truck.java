package calmsen.model.domain;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class Truck {
    private int width;
    private int height;
    private final List<Parcel> parcels;

    public Truck(int width, int height, Parcel parcel) {
        this.width = width;
        this.height = height;
        this.parcels = Collections.singletonList(parcel);
    }

    public Truck(int width, int height, List<Parcel> parcels) {
        this.width = width;
        this.height = height;
        this.parcels = parcels;
    }

    public void loadParcel(Parcel parcel) {
        this.parcels.add(parcel);
    }

    public boolean canLoadParcel(Parcel parcel) {
        return this.parcels.stream().mapToInt(Parcel::getHeight).sum() + parcel.getHeight() <= this.height;
    }

    public boolean isEmpty() {
        return this.parcels.isEmpty();
    }
}
