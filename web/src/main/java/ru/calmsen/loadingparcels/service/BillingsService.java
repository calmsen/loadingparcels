package ru.calmsen.loadingparcels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.OutboxMessage;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.dto.LoadParcelsBillingDto;
import ru.calmsen.loadingparcels.repository.OutboxRepository;
import ru.calmsen.loadingparcels.util.JsonUtil;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingsService {
    private final OutboxRepository outboxRepository;
    private final Clock clock;

    /**
     * Добавить счет за погрузку машин
     *
     * @param user   идентификатор машин
     * @param trucks погруженные машины
     */
    @Transactional
    public void addLoadParcelsBilling(String user, List<Truck> trucks){
        if (user == null || user.isEmpty()) {
            throw new BusinessException("Необходимо указать пользователя");
        }

        var message = toOutboxMessage(user, trucks, "loadParcelsBilling");
        outboxRepository.save(message);
    }

    /**
     * Добавить счет за разгрузку машин
     *
     * @param user   идентификатор машин
     * @param trucks погруженные машины
     */
    @Transactional
    public void addUnloadParcelsBilling(String user, List<Truck> trucks){
        if (user == null || user.isEmpty()) {
            throw new BusinessException("Необходимо указать пользователя");
        }

        var message = toOutboxMessage(user, trucks, "unloadParcelsBilling");
        outboxRepository.save(message);
    }

    private OutboxMessage toOutboxMessage(String user, List<Truck> trucks, String messageType) {
        return OutboxMessage.builder()
            .messageType(messageType)
            .payload(JsonUtil.toJson(toLoadParcelsBillingDto(user, trucks)))
            .createdAt(LocalDateTime.now(clock))
            .user(user)
            .build();
    }

    private LoadParcelsBillingDto toLoadParcelsBillingDto(String user, List<Truck> trucks) {
        return new LoadParcelsBillingDto(
                UUID.randomUUID(),
                user,
                (int) trucks.stream().filter(x -> !x.isEmpty()).count(),
                trucks.stream().mapToInt(x -> x.getParcels().size()).sum(),
                trucks.stream().mapToInt(Truck::getFilledPlaces).sum()
        );
    }
}
