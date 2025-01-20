package ru.calmsen.loadingparcels.repository;

import lombok.NoArgsConstructor;
import ru.calmsen.loadingparcels.model.domain.Billing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
public class BillingsRepository {
    private final List<Billing> billings = new ArrayList<>();

    /**
     * Добавить счет
     *
     * @param billing счет
     */
    public void addBilling(Billing billing) {
        billings.add(billing);
    }

    /**
     * Получить детали счетов
     *
     * @param user     идентификатор пользователя
     * @param fromDate дата от (включительно)
     * @param toDate   дата до (включительно)
     * @return список счетов
     */
    public List<Billing> getBillings(String user, LocalDate fromDate, LocalDate toDate) {
        return billings.stream()
                .filter(x -> x.getUser().equals(user))
                .filter(x -> !x.getDate().isBefore(fromDate))
                .filter(x -> !x.getDate().isAfter(toDate))
                .sorted(Comparator.comparing(Billing::getDate).reversed())
                .toList();
    }
}
