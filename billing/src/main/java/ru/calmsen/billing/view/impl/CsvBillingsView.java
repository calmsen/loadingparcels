package ru.calmsen.billing.view.impl;

import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.view.BillingsView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Csv представление для списка счетов
 */
public class CsvBillingsView implements BillingsView {
    /**
     * Возвращает данные для представления списка счетов в csv.
     *
     * @param billings список счетов
     * @return данные в виде csv
     */
    @Override
    public String buildOutputData(List<Billing> billings) {
        var output = new StringBuilder("Дата;Операция;Кол-во машин;Кол-во посылок;Счет(в рублях)");
        for (var billing : billings) {
            var parts = billing.getDescription().split(";");
            output.append("\n").append(parts[0]);
            output.append(";").append(parts[1]);
            output.append(";").append(parts[2].replace(" машин", ""));
            output.append(";").append(parts[3].replace(" посылок", ""));
            output.append(";").append(parts[4].replace(" рублей", ""));
        }

        return output.toString();
    }
}
