package ru.calmsen.service.loadingalgorithm;

import ru.calmsen.model.domain.Box;
import ru.calmsen.model.domain.Truck;
import ru.calmsen.model.domain.enums.BoxDimensionsType;
import ru.calmsen.model.domain.enums.BoxJoinType;
import ru.calmsen.model.domain.enums.LoadingMode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class EfficientLoadingAlgorithm implements LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.EFFICIENT;

    @Override
    public LoadingMode getMode() {
        return this.mode;
    }

    @Override
    public List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight) {
        boxes = packBoxes(boxes, truckWidth);

        List<Truck> trucks = new ArrayList<>();
        loadTrucks(boxes, trucks, truckWidth, truckHeight);
        return trucks;
    }

    private List<Box> packBoxes(List<Box> boxes, int truckWidth) {
        List<Box> newBoxes = new ArrayList<>();

        log.info("Упакуем блоки до 6X3");
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X3From3X3, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }

        log.info("Упакуем блоки до 6X2");
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X2From3X2, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X2From7, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X2From4X2, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }

        log.info("Упакуем блоки до 6X1");
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X1From5X1, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X1From4X1, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }

        log.info("Упакуем блоки до 6X6");
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X6From6X3, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X6From6X4, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X6From6X3, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }
        while (canJoinBoxes(BoxDimensionsType.Dimensions6X6From6X4, boxes, newBoxes, truckWidth)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }

        return boxes;
    }

    private void loadTrucks(List<Box> boxes, List<Truck> trucks, int truckWidth, int truckHeight) {
        var truckDimensions = truckWidth * truckHeight;
        log.info("Загрузим блоки 6X6");
        var readyToLoadBoxes = boxes.stream()
                .filter(x -> x.getDimensions() == truckDimensions)
                .toList();
        for (var box : readyToLoadBoxes) {
            trucks.add(new Truck(truckWidth, truckHeight, box));
        }

        log.info("Загрузим оставшееся блоки");
        var remainedBoxes = boxes.stream()
                .filter(x -> x.getDimensions() < truckDimensions)
                .sorted(Comparator.comparingInt(x -> x.getWidth(0))).toList();

        var currentTruck = new Truck(truckWidth, truckHeight, new ArrayList<>());
        for (var currentBox : remainedBoxes) {
            if (currentTruck.canLoadBox(currentBox)) {
                currentTruck.loadBox(currentBox);
            } else {
                trucks.add(currentTruck);
                currentTruck = new Truck(truckWidth, truckHeight, new ArrayList<>());
                currentTruck.loadBox(currentBox);
            }
        }

        if (!currentTruck.isEmpty()) {
            trucks.add(currentTruck);
        }
    }

    private boolean canJoinBoxes(
            BoxDimensionsType targetDimensions,
            List<Box> boxes,
            List<Box> newBoxes,
            int truckWidth) {

        if (!newBoxes.isEmpty()) {
            throw new IllegalArgumentException("Список newBoxes должен быть пустой.");
        }

        var firstDimensionType = targetDimensions.getFirstDimensionsForJoin();
        var secondDimensionType = targetDimensions.getSecondDimensionsForJoin();

        if (firstDimensionType == null || secondDimensionType == null) {
            return false;
        }
        var firstBox = boxes.stream()
                .filter(x -> x.getDimensions() == firstDimensionType.getDimensions() &&
                        (x.getDimensionsType() == null || x.getDimensionsType() == firstDimensionType))
                .findFirst().orElse(null);
        var secondBox = boxes.stream()
                .filter(x -> x != firstBox && x.getDimensions() == secondDimensionType.getDimensions() &&
                        (x.getDimensionsType() == null || x.getDimensionsType() == secondDimensionType))
                .findFirst().orElse(null);

        if (firstBox != null && secondBox != null) {
            var newBox = joinBoxes(firstBox, secondBox, targetDimensions.getJoinType(), truckWidth);
            if (newBox == null) {
                return false;
            }
            newBoxes.addAll(boxes.stream().filter(x -> x != firstBox && x != secondBox).toList());
            newBoxes.add(newBox);
            return true;
        }

        if (firstBox != null) {
            var boxesWithoutFirstBox = boxes.stream().filter(x -> x != firstBox).toList();
            List<Box> newInnerBoxes = new ArrayList<>();
            if (canJoinBoxes(secondDimensionType, boxesWithoutFirstBox, newInnerBoxes, truckWidth)) {
                var innerSecondBox = newInnerBoxes.getLast();
                var newBox = joinBoxes(firstBox, innerSecondBox, targetDimensions.getJoinType(), truckWidth);
                if (newBox == null) {
                    return false;
                }
                newBoxes.addAll(newInnerBoxes.stream().filter(x -> x != firstBox && x != innerSecondBox).toList());
                newBoxes.add(newBox);
                return true;
            }
        }

        if (secondBox != null) {
            var boxesWithoutSecondBox = boxes.stream().filter(x -> x != secondBox).toList();
            List<Box> newInnerBoxes = new ArrayList<>();
            if (canJoinBoxes(firstDimensionType, boxesWithoutSecondBox, newInnerBoxes, truckWidth)) {
                var innerFirstBox = newInnerBoxes.getLast();
                var newBox = joinBoxes(innerFirstBox, secondBox, targetDimensions.getJoinType(), truckWidth);
                if (newBox == null) {
                    return false;
                }
                newBoxes.addAll(newInnerBoxes.stream().filter(x -> x != secondBox && x != innerFirstBox).toList());
                newBoxes.add(newBox);
                return true;
            }
        }

        return false;
    }

    private Box joinBoxes(Box firstBox, Box secondBox, BoxJoinType mergeType, int truckWidth) {
        if (mergeType == BoxJoinType.HORIZONTAL) {
            if (firstBox.getHeight() != secondBox.getHeight()) {
                return null;
            }

            List<List<Character>> content = new ArrayList<>();
            for (int i = 0, j = 0; i < firstBox.getHeight(); i++) {
                if (firstBox.getWidth(i) + secondBox.getWidth(i) > truckWidth) {
                    return null;
                }

                content.add(Stream.concat(
                        firstBox.getContent().get(i).stream(),
                        secondBox.getContent().get(i).stream()
                ).toList());
            }

            return new Box(content, true);
        }

        var content = new ArrayList<>(firstBox.getContent());
        content.addAll(secondBox.getContent());
        return new Box(content, true);
    }
}
