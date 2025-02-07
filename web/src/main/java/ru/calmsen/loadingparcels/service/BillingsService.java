package ru.calmsen.loadingparcels.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.mapper.BillingMapper;
import ru.calmsen.loadingparcels.mapper.OutboxMapper;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.repository.OutboxRepository;
import ru.calmsen.loadingparcels.util.JsonUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingsService {
    private final BillingMapper billingMapper;
    private final OutboxService outboxService;

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

        var messagePayload = billingMapper.toParcelsBillingDto(operationType, user, trucks);
        outboxService.createMessage("addParcelsBilling", JsonUtil.toJson(messagePayload), user);
    }
}
