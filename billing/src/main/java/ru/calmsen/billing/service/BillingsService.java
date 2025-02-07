package ru.calmsen.billing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.calmsen.billing.config.BillingConfig;
import ru.calmsen.billing.exception.BusinessException;
import ru.calmsen.billing.mapper.BillingMapper;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.model.domain.InboxMessage;
import ru.calmsen.billing.model.dto.ParcelsBillingDto;
import ru.calmsen.billing.repository.BillingsRepository;
import ru.calmsen.billing.repository.InboxRepository;

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
    private final BillingMapper billingMapper;
    private final Clock clock;

    /**
     * Добавить счет за операцию погрузки/разгрузки машин
     *
     * @param message сообщение
     */
    @Transactional
    @CacheEvict(value = "billings", key = "#message.user + '-last-month'")
    public void addParcelsBilling(ParcelsBillingDto message) {
        if (inboxRepository.findById(message.getMessageId()).isPresent()) {
            return;
        }

        addBilling(message);
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

    private void addBilling(ParcelsBillingDto message) {
        billingsRepository.save(
                billingMapper.toBilling(message, calculateCost(message))
        );
    }

    private void addInboxMessage(UUID messageId) {
        var message = new InboxMessage();
        message.setId(messageId);
        message.setCreatedAt(LocalDateTime.now(clock));
        inboxRepository.save(message);
    }

    private BigDecimal calculateCost(ParcelsBillingDto message) {
        var costPerSegment = message.getOperationType().equals("Погрузка")
                ? billingConfig.getLoadingCostPerSegment()
                : billingConfig.getUnloadingCostPerSegment();
        return costPerSegment.multiply(BigDecimal.valueOf(message.getFilledPlaces()));
    }
}
