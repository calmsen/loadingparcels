package ru.calmsen.loadingparcels.model.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Truck {
    private final int width;
    private final int height;
    private final List<PlacedBox> boxes;

    public Truck(int width, int height) {
        this.width = width;
        this.height = height;
        this.boxes = new ArrayList<>();
    }

    public Truck(int width, int height, PlacedBox box) {
        this.width = width;
        this.height = height;
        this.boxes = List.of(box);
    }

    public void loadBox(PlacedBox box) {
        this.boxes.add(box);
    }

    public boolean isEmpty() {
        return this.boxes.isEmpty();
    }
}
