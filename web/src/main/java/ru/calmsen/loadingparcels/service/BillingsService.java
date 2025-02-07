package ru.calmsen.loadingparcels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.mapper.OutboxMapper;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.repository.OutboxRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingsService {
    private final OutboxRepository outboxRepository;
    private final OutboxMapper outboxMapper;

    /**
     * Добавить счет за операцию погрузки/разгрузки машин
     *
     * @param trucks погруженные машины
     * @param operationType тип операции
     * @param user   идентификатор машин
     */
    @Transactional
    public void addParcelsBilling(String operationType, String user, List<Truck> trucks){
        if (user == null || user.isEmpty()) {
            throw new BusinessException("Необходимо указать пользователя");
        }

        var message = outboxMapper.toOutboxMessage("addParcelsBilling", operationType, user, trucks);
        outboxRepository.save(message);
    }
}
