package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.PlacedParcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.dto.PlacedParcelDto;
import ru.calmsen.loadingparcels.model.dto.TruckDto;

import java.util.List;

/**
 * Маппер для моделей truck.
 */
@Mapper
public abstract class TrucksMapper {
    public abstract List<TruckDto> toTrucksDto(List<Truck> trucks);

    public abstract TruckDto toTruckDto(Truck truck);

    public abstract List<Truck> toTruckDomain(List<TruckDto> trucks);

    public Truck toTruckDomain(TruckDto truckDto) {
        var placedParcels = truckDto.getParcels().stream()
                .map(this::toPlacedParcelDomain)
                .toList();
        var truck = new Truck(truckDto.getWidth(), truckDto.getHeight());
        for (var placedParcel : placedParcels) {
            truck.loadParcel(placedParcel);
        }
        return truck;
    }

    private PlacedParcel toPlacedParcelDomain(PlacedParcelDto placedParcelDto) {
        return new PlacedParcel(
                new Parcel(
                        placedParcelDto.getParcel().getName(),
                        placedParcelDto.getParcel().getForm(),
                        placedParcelDto.getParcel().getSymbol()
                ),
                placedParcelDto.getPositionX(),
                placedParcelDto.getPositionY());
    }
}
