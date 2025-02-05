package ru.calmsen.billing.view.impl;

import lombok.RequiredArgsConstructor;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.util.JsonUtil;
import ru.calmsen.billing.view.BillingsView;

import java.util.List;

/**
 * Json представление для списка счетов
 */
@RequiredArgsConstructor
public class JsonBillingsView implements BillingsView {
    /**
     * Возвращает данные для представления списка счетов в json.
     *
     * @param billings список счетов
     * @return данные в виде json
     */
    @Override
    public String buildOutputData(List<Billing> billings) {
        return JsonUtil.toJson(billings);
    }
}
