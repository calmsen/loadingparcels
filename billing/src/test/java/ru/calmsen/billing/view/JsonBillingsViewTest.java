package ru.calmsen.billing.view;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.util.DateUtil;
import ru.calmsen.billing.view.impl.JsonBillingsView;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonBillingsViewTest {
    @Test
    void buildOutputData_SingleBilling_CorrectlyFormatsOutput() {
        // Arrange
        String description = String.format(Billing.DescriptionFormat, "20.01.2025", "Погрузка", 1, 1, 1.0);
        var singleBilling = Billing.builder()
                .id(1L)
                .user("user1")
                .description(description)
                .type("loadParcels")
                .date(DateUtil.toDate("20.01.2025"))
                .quantity(1)
                .cost(BigDecimal.valueOf(1.0))
                .build();
        List<Billing> billings = List.of(singleBilling);

        // Act
        String result = new JsonBillingsView().buildOutputData(billings);
        result = removeWhitespacesUsingGson(result);

        // Assert
        assertThat(result).isEqualTo("[{\"id\":1,\"user\":\"user1\",\"description\":\"20.01.2025;Погрузка;1 машин;1 посылок;1.00 рублей\",\"type\":\"loadParcels\",\"date\":\"2025-01-20\",\"quantity\":1,\"cost\":1.0}]");
    }

    @Test
    void buildOutputData_MultipleBillings_FormatsEachBillingSeparately() {
        // Arrange
        String description1 = String.format(Billing.DescriptionFormat, "20.01.2025", "Погрузка", 1, 1, 1.0);
        var billing1 = Billing.builder()
                .id(1L)
                .user("user1")
                .description(description1)
                .type("loadParcels")
                .date(DateUtil.toDate("20.01.2025"))
                .quantity(1)
                .cost(BigDecimal.valueOf(1.0))
                .build();
        String description2 = String.format(Billing.DescriptionFormat, "20.02.2025", "Разгрузка", 1, 1, 1.0);
        var billing2 = Billing.builder()
                .id(1L)
                .user("user1")
                .description(description2)
                .type("unloadParcels")
                .date(DateUtil.toDate("20.02.2025"))
                .quantity(1)
                .cost(BigDecimal.valueOf(1.0))
                .build();
        List<Billing> billings = List.of(billing1, billing2);

        // Act
        String result = new JsonBillingsView().buildOutputData(billings);
        result = removeWhitespacesUsingGson(result);

        // Assert
        var obj1 = "{\"id\":1,\"user\":\"user1\",\"description\":\"20.01.2025;Погрузка;1 машин;1 посылок;1.00 рублей\",\"type\":\"loadParcels\",\"date\":\"2025-01-20\",\"quantity\":1,\"cost\":1.0}";
        var obj2 = "{\"id\":1,\"user\":\"user1\",\"description\":\"20.02.2025;Разгрузка;1 машин;1 посылок;1.00 рублей\",\"type\":\"unloadParcels\",\"date\":\"2025-02-20\",\"quantity\":1,\"cost\":1.0}";
        assertThat(result).isEqualTo("["  + obj1 + "," + obj2 + "]");
    }

    private String removeWhitespacesUsingGson(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new StringSerializer()).create();
        JsonElement jsonElement = gson.fromJson(json, JsonElement.class);
        return gson.toJson(jsonElement);
    }

    static class StringSerializer implements JsonSerializer<String> {
        @Override
        public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.trim());
        }
    }
}