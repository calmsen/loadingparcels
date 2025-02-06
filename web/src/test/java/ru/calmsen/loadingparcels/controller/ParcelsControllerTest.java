package ru.calmsen.loadingparcels.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.calmsen.loadingparcels.container.AbstractPostgresContainer;
import ru.calmsen.loadingparcels.repository.OutboxRepository;
import ru.calmsen.loadingparcels.util.FileReader;

import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
class ParcelsControllerTest extends AbstractPostgresContainer {
    @Autowired
    MockMvc mockMvc;

    @MockitoSpyBean
    OutboxRepository outboxRepository;

    @Test
    void findParcels_withValidRequest_shouldReturnValidResponse() throws Exception {
        String expectedResponseJson = """
       [{
            "width": 3,
            "height": 3,
            "form": "xxx\\nxxx\\nxxx",
            "dimensions": 9,
            "symbol": "9",
            "name": "Посылка тип 9"
          },
          {
            "width": 4,
            "height": 2,
            "form": "xxxx\\nxxxx",
            "dimensions": 8,
            "symbol": "8",
            "name": "Посылка тип 8"
          },
          {
            "width": 4,
            "height": 2,
            "form": "xxx\\nxxxx",
            "dimensions": 7,
            "symbol": "7",
            "name": "Посылка тип 7"
          },
          {
            "width": 3,
            "height": 2,
            "form": "xxx\\nxxx",
            "dimensions": 6,
            "symbol": "6",
            "name": "Посылка тип 6"
          },
          {
            "width": 5,
            "height": 1,
            "form": "xxxxx",
            "dimensions": 5,
            "symbol": "5",
            "name": "Посылка тип 5"
          },
          {
            "width": 4,
            "height": 1,
            "form": "xxxx",
            "dimensions": 4,
            "symbol": "4",
            "name": "Посылка тип 4"
          },
          {
            "width": 3,
            "height": 1,
            "form": "xxx",
            "dimensions": 3,
            "symbol": "3",
            "name": "Посылка тип 3"
          },
          {
            "width": 2,
            "height": 1,
            "form": "xx",
            "dimensions": 2,
            "symbol": "2",
            "name": "Посылка тип 2"
          },
          {
            "width": 1,
            "height": 1,
            "form": "x",
            "dimensions": 1,
            "symbol": "1",
            "name": "Посылка тип 1"
          }]
       """;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/parcels"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(expectedResponseJson));
    }

    @Test
    void findParcel_withValidRequest_shouldReturnValidResponse() throws Exception {
        String expectedResponseJson = """
       [{
            "width": 1,
            "height": 1,
            "form": "x",
            "dimensions": 1,
            "symbol": "1",
            "name": "Посылка тип 1"
          }]
       """;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/parcels/Посылка тип 1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(expectedResponseJson));
    }

    @Test
    @Rollback
    void createParcel_withValidRequest_shouldReturnValidResponse() throws Exception {
        String requestJson = """
       {
            "form": "x",
            "symbol": "1",
            "name": "Копия Посылка тип 1"
          }
       """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parcels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Rollback
    void updateParcel_withValidRequest_shouldReturnValidResponse() throws Exception {
        String requestJson = """
       {
            "form": "o",
            "symbol": "1",
            "name": "Посылка тип 1"
          }
       """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/parcels/Посылка тип 1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Rollback
    void deleteParcel_withValidRequest_shouldReturnValidResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/parcels/Посылка тип 1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void loadParcels_withValidRequest_shouldReturnValidResponse() throws Exception {
        String requestJson = """
       {
            "parcelNames": ["Посылка тип 1", "Посылка тип 2"],
            "user": "user1"
          }
       """;

        String expectedResponseJson = """
       [{
            "width": 6,
            "height": 6,
            "parcels": [
              {
                "parcel": {
                  "width": 1,
                  "height": 1,
                  "form": "x",
                  "dimensions": 1,
                  "symbol": "1",
                  "name": "Посылка тип 1"
                },
                "positionX": 0,
                "positionY": 0
              }
            ]
          },
          {
            "width": 6,
            "height": 6,
            "parcels": [
              {
                "parcel": {
                  "width": 2,
                  "height": 1,
                  "form": "xx",
                  "dimensions": 2,
                  "symbol": "2",
                  "name": "Посылка тип 2"
                },
                "positionX": 0,
                "positionY": 0
              }
            ]
          }]
       """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parcels/actions/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(expectedResponseJson));

        Mockito.verify(outboxRepository).save(argThat(message ->
                message.getUser().equals("user1") &&
                message.getMessageType().equals("loadParcelsBilling") &&
                message.getPayload().contains("\"user\": \"user1\"") &&
                message.getPayload().contains("\"trucksCount\": 2") &&
                message.getPayload().contains("\"parcelsCount\": 2") &&
                message.getPayload().contains("\"filledPlaces\": 3"))
        );
    }
}

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
class WithFileReaderMock extends AbstractPostgresContainer {
    @Autowired
    MockMvc mockMvc;

    @MockitoSpyBean
    OutboxRepository outboxRepository;

    @MockitoBean
    FileReader fileReader;

    @Test
    void unloadParcels_withValidRequest_shouldReturnValidResponse() throws Exception {
        String requestJson = """
       {
            "inFile": "some_file.json",
            "user": "user1"
          }
       """;

        String fileContent = """
        [{
            "width": 6,
            "height": 6,
            "parcels": [
              {
                "parcel": {
                  "width": 3,
                  "height": 3,
                  "form": "xxx\\nxxx\\nxxx",
                  "dimensions": 9,
                  "symbol": "9",
                  "name": "Посылка тип 9"
                },
                "positionX": 0,
                "positionY": 0
              },
              {
                "parcel": {
                  "width": 3,
                  "height": 3,
                  "form": "xxx\\nxxx\\nxxx",
                  "dimensions": 9,
                  "symbol": "9",
                  "name": "Посылка тип 9"
                },
                "positionX": 3,
                "positionY": 0
              }
            ]
          }]
       """;



        String expectedResponseJson = """
       [{
            "width": 3,
            "height": 3,
            "form": "xxx\\nxxx\\nxxx",
            "dimensions": 9,
            "symbol": "9",
            "name": "Посылка тип 9"
          },
          {
            "width": 3,
            "height": 3,
            "form": "xxx\\nxxx\\nxxx",
            "dimensions": 9,
            "symbol": "9",
            "name": "Посылка тип 9"
          }]
       """;

        Mockito.when(fileReader.readString("some_file.json")).thenReturn(fileContent);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/parcels/actions/unload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(expectedResponseJson));

        Mockito.verify(outboxRepository).save(argThat(message ->
                message.getUser().equals("user1") &&
                        message.getMessageType().equals("unloadParcelsBilling") &&
                        message.getPayload().contains("\"user\": \"user1\"") &&
                        message.getPayload().contains("\"trucksCount\": 1") &&
                        message.getPayload().contains("\"parcelsCount\": 2") &&
                        message.getPayload().contains("\"filledPlaces\": 18")
        ));
    }
}