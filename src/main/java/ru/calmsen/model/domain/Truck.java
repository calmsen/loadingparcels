package ru.calmsen.model.domain;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class Truck {
    private int width;
    private int height;
    private final List<Box> boxes;

    public Truck(int width, int height, Box box) {
        this.width = width;
        this.height = height;
        this.boxes = Collections.singletonList(box);
    }

    public Truck(int width, int height, List<Box> boxes) {
        this.width = width;
        this.height = height;
        this.boxes = boxes;
    }

    public void loadBox(Box box) {
        this.boxes.add(box);
    }

    public boolean canLoadBox(Box box) {
        return this.boxes.stream().mapToInt(Box::getHeight).sum() + box.getHeight() <= this.height
                && box.getContent().stream().allMatch(x -> x.size() <= this.width);
    }

    public boolean isEmpty() {
        return this.boxes.isEmpty();
    }
}
