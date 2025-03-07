package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import ru.calmsen.loadingparcels.model.domain.OutboxMessage;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.dto.ParcelsBillingDto;
import ru.calmsen.loadingparcels.util.JsonUtil;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class OutboxMapper {
    @Autowired
    protected Clock clock;

    public OutboxMessage toOutboxMessage(String messageType, String payload, String user) {
        return OutboxMessage.builder()
                .messageType(messageType)
                .payload(payload)
                .createdAt(LocalDateTime.now(clock))
                .user(user)
                .build();
    }

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
