package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.PlacedBox;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.dto.PlacedBoxDto;
import ru.calmsen.loadingparcels.model.dto.TruckDto;

import java.util.ArrayList;
import java.util.List;

@Mapper
public abstract class TrucksMapper {
    public abstract List<TruckDto> toTrucksDto(List<Truck> trucks);
    public abstract TruckDto toTruckDto(Truck truck);

    public abstract List<Truck> toTruckDomain(List<TruckDto> trucks);

    public Truck toTruckDomain(TruckDto truckDto) {
        var placedBoxes = truckDto.getBoxes().stream()
                .map(this::toPlacedBoxDomain)
                .toList();
        var truck = new Truck(truckDto.getWidth(), truckDto.getHeight());
        for (var placeBox : placedBoxes) {
            truck.loadBox(placeBox);
        }
        return truck;
    }

    private PlacedBox toPlacedBoxDomain(PlacedBoxDto placedBoxDto) {
        var charSymbol = (Character) (char)(placedBoxDto.getBox().getDimensions() + '0');
        List<List<Character>> boxContent = new ArrayList<>();
        var currentDimensions = 0;
        for (int i = 0; i < placedBoxDto.getBox().getHeight(); i++) {
            List<Character> row = new ArrayList<>();
            for (int j = 0; j < placedBoxDto.getBox().getWidth(); j++) {
                if (currentDimensions == placedBoxDto.getBox().getDimensions()) {
                    continue;
                }
                row.add(charSymbol);
                currentDimensions++;
            }
            boxContent.add(row);
        }

        var box = new Box(boxContent.reversed());
        return new PlacedBox(
                box,
                placedBoxDto.getPositionX(),
                placedBoxDto.getPositionY());
    }
}
