package ru.calmsen.billing.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.calmsen.billing.container.AbstractPostgresContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class BillingsControllerTest extends AbstractPostgresContainer {
    @Autowired
    MockMvc mockMvc;

    @Test
    void billing_withValidRequest_shouldReturnValidResponse() throws Exception {
        String expectedResponseJson = """
       [{
            "id": 1,
            "user": "user1",
            "description": "03.02.2025;Погрузка;34 машин;34 посылок;11120.00 рублей",
            "type": "loadParcels",
            "date": "2025-02-04",
            "quantity": 139,
            "cost": 11120.00
          },
          {
            "id": 2,
            "user": "user1",
            "description": "03.02.2025;Разгрузка;4 машин;34 посылок;6950.00 рублей",
            "type": "unloadParcels",
            "date": "2025-02-04",
            "quantity": 139,
            "cost": 6950.00
          }]
       """;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/billing")
                        .queryParam("user", "user1")
                        .queryParam("from", "04.01.2025")
                        .queryParam("to", "04.02.2025"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(expectedResponseJson));
    }
}