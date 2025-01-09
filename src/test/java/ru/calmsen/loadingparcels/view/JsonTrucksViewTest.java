package ru.calmsen.loadingparcels.view;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.mapper.TrucksMapperImpl;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.PlacedParcel;
import ru.calmsen.loadingparcels.model.domain.Truck;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTrucksViewTest {
    @Test
    void getOutputData_emptyTrucks_returnsEmptyArrayInJson() {
        // Arrange
        var view = new JsonTrucksView(new TrucksMapperImpl());

        // Act
        var result = view.getOutputData(List.of());

        // Assert
        assertEquals("[]", result);
    }

    @Test
    void getOutputData_truckWithEmptyParcels_returnsEmptyParcelsArrayInJson() {
        // Arrange
        var view = new JsonTrucksView(new TrucksMapperImpl());
        var truck = new Truck(8, 8);

        // Act
        var result = removeWhitespacesUsingGson(view.getOutputData(List.of(truck)));

        // Assert
        assertEquals("[{\"width\":8,\"height\":8,\"parcels\":[]}]", result);
    }

    @Test
    void getOutputData_truckWithParcel_returnsParcelInfoInJson() {
        // Arrange
        var view = new JsonTrucksView(new TrucksMapperImpl());
        var parcel = new Parcel(List.of(
                List.of('9', '9', '9'),
                List.of('9', '9', '9'),
                List.of('9', '9', '9')
        ));
        var truck = new Truck(8, 8, new PlacedParcel(parcel, 3, 3));

        // Act
        var result = removeWhitespacesUsingGson(view.getOutputData(List.of(truck)));

        // Assert
        assertEquals("[{\"width\":8,\"height\":8,\"parcels\":[{\"parcel\":{\"width\":3,\"height\":3,\"form\":\"xxx\\nxxx\\nxxx\",\"dimensions\":9,\"symbol\":\"9\",\"name\":\"Посылка тип 9\"},\"positionX\":3,\"positionY\":3}]}]", result);
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