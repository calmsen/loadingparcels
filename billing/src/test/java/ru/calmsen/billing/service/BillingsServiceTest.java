package ru.calmsen.billing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.calmsen.billing.config.BillingConfig;
import ru.calmsen.billing.model.domain.InboxMessage;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.model.dto.LoadParcelsBillingDto;
import ru.calmsen.billing.repository.BillingsRepository;
import ru.calmsen.billing.repository.InboxRepository;
import ru.calmsen.billing.exception.BusinessException;

import java.math.BigDecimal;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BillingsServiceTest {

    @Mock
    private BillingConfig billingConfig;
    @Mock
    private BillingsRepository billingsRepository;
    @Mock
    private InboxRepository inboxRepository;

    private Clock fixedClock;
    private BillingsService billingsService;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2023-06-15T10:00:00Z"), ZoneId.systemDefault());
        billingsService = new BillingsService(billingConfig, billingsRepository, inboxRepository, fixedClock);
    }

    @Test
    void addLoadParcelsBilling_NewMessage_ShouldSaveBillingAndInboxMessage() {
        UUID messageId = UUID.randomUUID();
        LoadParcelsBillingDto message = new LoadParcelsBillingDto(messageId, "user1", 2, 10, 5);
        BigDecimal loadingCost = BigDecimal.valueOf(10);

        when(inboxRepository.findById(messageId)).thenReturn(Optional.empty());
        when(billingConfig.getLoadingCostPerSegment()).thenReturn(loadingCost);

        billingsService.addLoadParcelsBilling(message);

        verify(billingsRepository).save(argThat(billing ->
                billing.getUser().equals("user1") &&
                        billing.getDescription().contains("Погрузка") &&
                        billing.getType().equals("loadParcels") &&
                        billing.getDate().equals(LocalDate.now(fixedClock)) &&
                        billing.getQuantity() == 5 &&
                        billing.getCost().compareTo(loadingCost.multiply(BigDecimal.valueOf(5))) == 0
        ));

        verify(inboxRepository).save(argThat(inboxMessage ->
                inboxMessage.getId().equals(messageId) &&
                        inboxMessage.getOwner().equals("user1") &&
                        inboxMessage.getCreatedAt().equals(LocalDateTime.now(fixedClock))
        ));
    }

    @Test
    void addLoadParcelsBilling_ExistingMessage_ShouldNotSaveBillingOrInboxMessage() {
        UUID messageId = UUID.randomUUID();
        LoadParcelsBillingDto message = new LoadParcelsBillingDto(messageId, "user1", 2, 10, 5);

        when(inboxRepository.findById(messageId)).thenReturn(Optional.of(new InboxMessage()));

        billingsService.addLoadParcelsBilling(message);

        verify(billingsRepository, never()).save(any());
        verify(inboxRepository, never()).save(any());
    }

    @Test
    void addUnloadParcelsBilling_NewMessage_ShouldSaveBillingAndInboxMessage() {
        UUID messageId = UUID.randomUUID();
        LoadParcelsBillingDto message = new LoadParcelsBillingDto(messageId, "user1", 2, 10, 5);
        BigDecimal unloadingCost = BigDecimal.valueOf(15);

        when(inboxRepository.findById(messageId)).thenReturn(Optional.empty());
        when(billingConfig.getUnloadingCostPerSegment()).thenReturn(unloadingCost);

        billingsService.addUnloadParcelsBilling(message);

        verify(billingsRepository).save(argThat(billing ->
                billing.getUser().equals("user1") &&
                        billing.getDescription().contains("Разгрузка") &&
                        billing.getType().equals("unloadParcels") &&
                        billing.getDate().equals(LocalDate.now(fixedClock)) &&
                        billing.getQuantity() == 5 &&
                        billing.getCost().compareTo(unloadingCost.multiply(BigDecimal.valueOf(5))) == 0
        ));

        verify(inboxRepository).save(argThat(inboxMessage ->
                inboxMessage.getId().equals(messageId) &&
                        inboxMessage.getOwner().equals("user1") &&
                        inboxMessage.getCreatedAt().equals(LocalDateTime.now(fixedClock))
        ));
    }

    @Test
    void addUnloadParcelsBilling_ExistingMessage_ShouldNotSaveBillingOrInboxMessage() {
        UUID messageId = UUID.randomUUID();
        LoadParcelsBillingDto message = new LoadParcelsBillingDto(messageId, "user1", 2, 10, 5);

        when(inboxRepository.findById(messageId)).thenReturn(Optional.of(new InboxMessage()));

        billingsService.addUnloadParcelsBilling(message);

        verify(billingsRepository, never()).save(any());
        verify(inboxRepository, never()).save(any());
    }

    @Test
    void findBillings_ValidInput_ShouldReturnBillings() {
        // Arrange
        String user = "testUser";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        List<Billing> expectedBillings = Arrays.asList(new Billing(), new Billing());

        when(billingsRepository.findAllByUserAndDateBetweenOrderByDateDesc(user, fromDate, toDate))
                .thenReturn(expectedBillings);

        // Act
        List<Billing> result = billingsService.findBillings(user, fromDate, toDate);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedBillings)
                .hasSize(2);

        verify(billingsRepository).findAllByUserAndDateBetweenOrderByDateDesc(user, fromDate, toDate);
    }

    @Test
    void findBillings_NullUser_ShouldThrowBusinessException() {
        // Arrange
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);

        // Act & Assert
        assertThatThrownBy(() -> billingsService.findBillings(null, fromDate, toDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Необходимо указать идентификатор пользователя");

        verifyNoInteractions(billingsRepository);
    }

    @Test
    void findBillings_EmptyUser_ShouldThrowBusinessException() {
        // Arrange
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);

        // Act & Assert
        assertThatThrownBy(() -> billingsService.findBillings("", fromDate, toDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Необходимо указать идентификатор пользователя");

        verifyNoInteractions(billingsRepository);
    }

    @Test
    void findBillings_NullDates_ShouldPassNullToRepository() {
        // Arrange
        String user = "testUser";
        when(billingsRepository.findAllByUserAndDateBetweenOrderByDateDesc(user, null, null))
                .thenReturn(Arrays.asList(new Billing()));

        // Act
        List<Billing> result = billingsService.findBillings(user, null, null);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(1);

        verify(billingsRepository).findAllByUserAndDateBetweenOrderByDateDesc(user, null, null);
    }
}
