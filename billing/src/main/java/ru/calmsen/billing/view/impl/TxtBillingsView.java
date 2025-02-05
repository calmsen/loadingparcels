package ru.calmsen.billing.view.impl;

import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.view.BillingsView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Текстовое представление для списка счетов
 */
public class TxtBillingsView implements BillingsView {
    /**
     * Возвращает данные для представления списка счетов в txt.
     *
     * @param billings список счетов
     * @return данные в виде txt
     */
    @Override
    public String buildOutputData(List<Billing> billings) {
        return billings.stream()
                .map(Billing::getDescription)
                .collect(Collectors.joining("\n"));
    }
}
