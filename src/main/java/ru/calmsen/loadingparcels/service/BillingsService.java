package ru.calmsen.loadingparcels.service;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.config.BillingConfig;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Billing;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.repository.BillingsRepository;
import ru.calmsen.loadingparcels.util.DateUtil;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class BillingsService {
    private final BillingConfig billingConfig;
    private final BillingsRepository billingsRepository;
    private final Clock clock;

    /**
     * Добавить счет за погрузку машин
     *
     * @param user   идентификатор машин
     * @param trucks погруженные машины
     */
    public void addLoadParcelsBilling(String user, List<Truck> trucks) {
        if (user == null || user.isEmpty()) {
            throw new BusinessException("Необходимо указать пользователя");
        }

        var segments = trucks.stream().mapToInt(Truck::getFilledPlaces).sum();
        var cost = billingConfig.getLoadingCostPerSegment().multiply(BigDecimal.valueOf(segments));
        LocalDate date = LocalDate.now(clock);
        billingsRepository.addBilling(new Billing(
                user,
                String.format(
                        "%s;Погрузка;%d машин;%d посылок;%.2f рублей",
                        DateUtil.toString(date),
                        trucks.stream().filter(x -> !x.isEmpty()).count(),
                        trucks.stream().mapToInt(x -> x.getParcels().size()).sum(),
                        cost
                ),
                "loadParcels",
                date,
                segments,
                cost
        ));
    }

    /**
     * Добавить счет за разгрузку машин
     *
     * @param user   идентификатор машин
     * @param trucks погруженные машины
     */
    public void addUnloadParcelsBilling(String user, List<Truck> trucks) {
        if (user == null || user.isEmpty()) {
            throw new BusinessException("Необходимо указать идентификатор пользователя");
        }

        var segments = trucks.stream().mapToInt(Truck::getFilledPlaces).sum();
        var cost = billingConfig.getUnloadingCostPerSegment().multiply(BigDecimal.valueOf(segments));
        LocalDate date = LocalDate.now(clock);
        billingsRepository.addBilling(new Billing(
                user,
                String.format(
                        "%s;Разгрузка;%d машин;%d посылок;%.2f рублей",
                        DateUtil.toString(date),
                        trucks.size(),
                        trucks.stream().mapToInt(x -> x.getParcels().size()).sum(),
                        cost
                ),
                "unloadParcels",
                date,
                segments,
                cost
        ));
    }

    public List<Billing> getBillings(String user, LocalDate fromDate, LocalDate toDate) {
        if (user == null || user.isEmpty()) {
            throw new BusinessException("Необходимо указать идентификатор пользователя");
        }

        return billingsRepository.getBillings(user, fromDate, toDate);
    }

}
