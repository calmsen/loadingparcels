package ru.calmsen.loadingparcels.service.loadingalgorithm;

import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.PlacedBox;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.DimensionsType;
import ru.calmsen.loadingparcels.model.domain.enums.JoinType;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Максимально плотная погрузка.
 */
@Slf4j
public class EfficientLoadingAlgorithm implements LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.EFFICIENT;

    @Override
    public LoadingMode getMode() {
        return this.mode;
    }

    @Override
    public List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight, int trucksCount) {
        if (boxes == null || boxes.isEmpty()) {
            return new ArrayList<>();
        }

        TruckLoaderHelper.checkMinTrucksCountBeforeLoad(boxes, truckWidth, truckHeight, trucksCount);

        boxes = packBoxes(boxes, truckWidth, truckHeight);

        List<Truck> trucks = new ArrayList<>();

        var truckDimensions = truckWidth * truckHeight;
        log.info("Загрузим сортированные посылки");
        var readyToLoadBoxes = boxes.stream()
                .filter(x -> x.getDimensions() == truckDimensions)
                .toList();
        for (var box : readyToLoadBoxes) {
            var truck = new Truck(truckWidth, truckHeight);
            var placedBoxes = flatPlacedBoxes(new PlacedBox(box)).toList();
            for (var placedBox : placedBoxes) {
                truck.loadBox(placedBox);
            }
            trucks.add(truck);
        }

        log.info("Загрузим оставшееся посылки");
        boxes = boxes.stream()
                .filter(x -> x.getDimensions() < truckDimensions)
                .sorted(Comparator.comparingInt(Box::getDimensions).reversed())
                .toList();
        trucks.addAll(TruckLoaderHelper.loadBoxes(boxes, truckWidth, truckHeight));

        TruckLoaderHelper.checkMinTrucksCountAfterLoad(trucksCount, trucks);
        return trucks;
    }

    private List<Box> packBoxes(List<Box> boxes, int truckWidth, int truckHeight) {
        // отсортируем коробки по размерности
        boxes = boxes.stream().sorted(Comparator.comparingInt(Box::getDimensions).reversed()).collect(Collectors.toList());

//        var capacity = boxes.stream().mapToInt(Box::getDimensions).sum();
//        log.info("Весь объем равен: {}", capacity);

        List<Box> newBoxes = new ArrayList<>();
        while (canJoinBoxes(DimensionsType.Dimensions6X6, boxes, newBoxes, truckWidth, truckHeight)) {
            boxes = newBoxes;
            newBoxes = new ArrayList<>();
        }

//         var capacityAfterPack = boxes.stream().mapToInt(Box::getDimensions).sum();
//         log.info("Весь объем после сортировки равен: {}", capacityAfterPack);

        return boxes;
    }

    private boolean canJoinBoxes(
            DimensionsType targetDimensions,
            List<Box> boxes,
            List<Box> newBoxes,
            int truckWidth,
            int truckHeight) {

        if (!newBoxes.isEmpty()) {
            throw new IllegalArgumentException("Список newBoxes должен быть пустой.");
        }

        for (var i = 0; i < targetDimensions.getJoinPairs().length; i++) {
            var joinPair = targetDimensions.getJoinPairs()[i];
            var firstDimensionType = joinPair.getFirstDimensions();
            var secondDimensionType = joinPair.getSecondDimensions();

            var firstBox = findBox(boxes, firstDimensionType, null);
            if (firstBox.isEmpty()) {
                List<Box> newInnerBoxes = new ArrayList<>();
                if (canJoinBoxes(firstDimensionType, boxes, newInnerBoxes, truckWidth, truckHeight)) {
                    boxes = newInnerBoxes;
                    i--; //повторяем повторно, но уже с измененным списком
                }
                continue;
            }
            var secondBox = findBox(boxes, secondDimensionType, firstBox.get());
            if (secondBox.isEmpty()) {
                var boxesWithoutFirstBox = boxes.stream().filter(x -> x != firstBox.get()).toList();
                List<Box> newInnerBoxes = new ArrayList<>();
                if (canJoinBoxes(secondDimensionType, boxesWithoutFirstBox, newInnerBoxes, truckWidth, truckHeight)) {
                    boxes = newInnerBoxes;
                    newInnerBoxes.add(firstBox.get()); // возвращаем первый бокс
                    i--; //повторяем повторно, но уже с измененным списком
                }
                continue;
            }

            if (canJoinBoxes(firstBox.get(), secondBox.get(), joinPair.getJoinType(), truckWidth, truckHeight)) {
                var newBox = new Box(firstBox.get(), secondBox.get(), joinPair.getJoinType());
                newBoxes.addAll(boxes.stream().filter(x -> x != firstBox.get() && x != secondBox.get()).toList());
                newBoxes.add(newBox);
                return true;
            }
        }

        return false;
    }

    private boolean canJoinBoxes(Box firstBox, Box secondBox, JoinType joinType, int truckWidth, int truckHeight) {
        if (joinType == JoinType.VERTICAL) {
            return firstBox.getHeight() + secondBox.getHeight() <= truckHeight;
        }

        if (firstBox.getHeight() != secondBox.getHeight()) {
            log.warn("Боксы не совпадают по высоте: {}, {}", firstBox.getHeight(), secondBox.getHeight());
            return false;
        }

        for (int i = 0, j = 0; i < firstBox.getHeight(); i++) {
            if (firstBox.getWidth(i) + secondBox.getWidth(i) <= truckWidth) {
                continue;
            }

            log.warn("Боксы не совпадают по ширине: {}, {}", firstBox.getWidth(i), secondBox.getWidth(i));
            return false;
        }

        return true;
    }

    private Optional<Box> findBox(List<Box> boxes, DimensionsType dimensionType, Box exclude) {
        for (Box x : boxes) {
            if (x.getDimensions() == dimensionType.getDimensions() &&
                    //x.getWidth(0) == dimensionType.getWidth() &&
                    x.getHeight() == dimensionType.getHeight() &&
                    x != exclude) {
                return Optional.of(x);
            }
        }

        return Optional.empty();
    }

    private Stream<PlacedBox> flatPlacedBoxes(PlacedBox placeBox) {
        if (placeBox.getBox().getInner().isEmpty()) {
            return Stream.of(placeBox);
        }

        if (placeBox.getBox().getInner().size() > 2) {
            throw new IllegalArgumentException();
        }

        var x = placeBox.getPositionX();
        var y = placeBox.getPositionY();

        var firstBox = new PlacedBox(placeBox.getBox().getInner().get(0), x, y);
        var secondBox = new PlacedBox(placeBox.getBox().getInner().get(1), x, y);

        if (placeBox.getBox().getJoinType() == JoinType.HORIZONTAL) {
            secondBox.setPositionX(x + firstBox.getBox().getWidth(0));
        } else {
            firstBox.setPositionY(y + secondBox.getBox().getHeight());

            var offsetX = placeBox.getBox().getWidth(0) - secondBox.getBox().getWidth(0);
            secondBox.setPositionX(x + offsetX);
        }

        return Stream.of(firstBox, secondBox).flatMap(this::flatPlacedBoxes);
    }
}
