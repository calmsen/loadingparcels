package ru.calmsen.billing.view;

import org.junit.jupiter.api.Test;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.view.impl.CsvBillingsView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvBillingsViewTest {
    @Test
    void buildOutputData_SingleBilling_CorrectlyFormatsOutput() {
        // Arrange
        String description = String.format(Billing.DescriptionFormat, "20.01.2025", "Погрузка", 1, 1, 1.0);
        var singleBilling = Billing.builder()
                .description(description)
                .build();
        List<Billing> billings = List.of(singleBilling);

        // Act
        String result = new CsvBillingsView().buildOutputData(billings);

        // Assert
        var row = toRow(description);
        assertThat(result).isEqualTo("Дата;Операция;Кол-во машин;Кол-во посылок;Счет(в рублях)\n" + row);
    }

    @Test
    void buildOutputData_MultipleBillings_FormatsEachBillingSeparately() {
        // Arrange
        String description1 = String.format(Billing.DescriptionFormat, "20.01.2025", "Погрузка", 1, 1, 1.0);
        var billing1 = Billing.builder()
                .description(description1)
                .build();
        String description2 = String.format(Billing.DescriptionFormat, "20.02.2025", "Разгрузка", 1, 1, 1.0);
        var billing2 = Billing.builder()
                .description(description2)
                .build();
        List<Billing> billings = List.of(billing1, billing2);

        // Act
        String result = new CsvBillingsView().buildOutputData(billings);

        // Assert
        var row1 = toRow(description1);
        var row2 = toRow(description2);
        assertThat(result).isEqualTo("Дата;Операция;Кол-во машин;Кол-во посылок;Счет(в рублях)\n" + row1 + "\n" + row2);
    }

    private static String toRow(String description1) {
        return description1
                .replace(" машин", "")
                .replace(" посылок", "")
                .replace(" рублей", "");
    }
}