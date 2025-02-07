package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.dto.ParcelsBillingDto;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BillingMapper {
    public ParcelsBillingDto toParcelsBillingDto(String operationType, String user, List<Truck> trucks) {
        return new ParcelsBillingDto(
                UUID.randomUUID(),
                user,
                (int) trucks.stream().filter(x -> !x.isEmpty()).count(),
                trucks.stream().mapToInt(x -> x.getParcels().size()).sum(),
                trucks.stream().mapToInt(Truck::getFilledPlaces).sum(),
                operationType
        );
    }
}
