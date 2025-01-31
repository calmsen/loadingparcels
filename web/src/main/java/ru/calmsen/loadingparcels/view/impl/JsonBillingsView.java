package ru.calmsen.loadingparcels.view.impl;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.model.domain.Billing;
import ru.calmsen.loadingparcels.util.JsonUtil;
import ru.calmsen.loadingparcels.view.BillingsView;

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
