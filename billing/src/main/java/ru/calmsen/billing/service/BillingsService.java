package ru.calmsen.billing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.calmsen.billing.config.BillingConfig;
import ru.calmsen.billing.exception.BusinessException;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.model.domain.InboxMessage;
import ru.calmsen.billing.model.dto.LoadParcelsBillingDto;
import ru.calmsen.billing.repository.BillingsRepository;
import ru.calmsen.billing.repository.InboxRepository;
import ru.calmsen.billing.util.DateUtil;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingsService {
    private final BillingConfig billingConfig;
    private final BillingsRepository billingsRepository;
    private final InboxRepository inboxRepository;
    private final Clock clock;

    /**
     * Добавить счет за погрузку машин
     *
     * @param message   сообщение
     */
    @Transactional
    @CacheEvict(value = "billings", key = "#message.user + '-last-month'")
    public void addLoadParcelsBilling(LoadParcelsBillingDto message) {
        if (inboxRepository.findById(message.getMessageId()).isPresent()) {
            return;
        }

        billingsRepository.save(
                toBilling(message, billingConfig.getLoadingCostPerSegment(), "Погрузка")
        );

        addInboxMessage(message.getMessageId());
    }

    /**
     * Добавить счет за разгрузку машин
     *
     * @param message   сообщение
     */
    @Transactional
    @CacheEvict(value = "billings", key = "#message.user + '-last-month'")
    public void addUnloadParcelsBilling(LoadParcelsBillingDto message) {
        if (inboxRepository.findById(message.getMessageId()).isPresent()) {
            return;
        }

        billingsRepository.save(
                toBilling(message, billingConfig.getUnloadingCostPerSegment(), "Разгрузка")
        );

        addInboxMessage(message.getMessageId());
    }

    public List<Billing> findBillings(String user, LocalDate fromDate, LocalDate toDate) {
        if (user == null || user.isEmpty()) {
            throw new BusinessException("Необходимо указать идентификатор пользователя");
        }

        return billingsRepository.findAllByUserAndDateBetweenOrderByDateDesc(user, fromDate, toDate);
    }

    @Cacheable(value = "billings", key = "#user + '-last-month'")
    public List<Billing> findBillingsForLastMonth(String user) {
        if (user == null || user.isEmpty()) {
            throw new BusinessException("Необходимо указать идентификатор пользователя");
        }
        LocalDate toDate = LocalDate.now(clock);
        LocalDate fromDate = toDate.minusDays(30);

        return billingsRepository.findAllByUserAndDateBetweenOrderByDateDesc(user, fromDate, toDate);
    }

    private void addInboxMessage(UUID messageId) {
        var message = new InboxMessage();
        message.setId(messageId);
        message.setCreatedAt(LocalDateTime.now(clock));
        inboxRepository.save(message);
    }

    private Billing toBilling(LoadParcelsBillingDto message, BigDecimal costPerSegment, String operationType) {
        var cost = costPerSegment.multiply(BigDecimal.valueOf(message.getFilledPlaces()));
        LocalDate date = LocalDate.now(clock);
        return Billing.builder()
                .user(message.getUser())
                .description(toDescription(message, operationType, date, cost))
                .type(operationType)
                .date(date)
                .quantity(message.getFilledPlaces())
                .cost(cost)
                .build();
    }

    private String toDescription(LoadParcelsBillingDto message, String operationType, LocalDate date, BigDecimal cost) {
        return String.format(
                Billing.DescriptionFormat,
                operationType,
                DateUtil.toString(date),
                message.getTrucksCount(),
                message.getParcelsCount(),
                cost
        );
    }
}
