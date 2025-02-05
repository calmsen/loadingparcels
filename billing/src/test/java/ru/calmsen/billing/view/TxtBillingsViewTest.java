package ru.calmsen.billing.view;

import org.junit.jupiter.api.Test;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.view.impl.TxtBillingsView;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

class TxtBillingsViewTest {
    @Test
    void buildOutputData_SingleBilling_CorrectlyFormatsOutput() {
        // Arrange
        String description = String.format(Billing.DescriptionFormat, "20.01.2025", "Погрузка", 1, 1, 1.0);
        var singleBilling = Billing.builder()
                .description(description)
                .build();
        List<Billing> billings = List.of(singleBilling);

        // Act
        String result = new TxtBillingsView().buildOutputData(billings);

        // Assert
        assertThat(result).isEqualTo(description);
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
        String result = new TxtBillingsView().buildOutputData(billings);

        // Assert
        assertThat(result).isEqualTo(description1 + "\n" + description2);
    }
}