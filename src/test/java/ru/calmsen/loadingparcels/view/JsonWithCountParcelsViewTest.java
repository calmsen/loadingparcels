package ru.calmsen.loadingparcels.view;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.mapper.ParcelsMapperImpl;
import ru.calmsen.loadingparcels.model.domain.Parcel;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonWithCountParcelsViewTest {
    @Test
    void getOutputData_emptyParcels_returnsEmptyArrayInJson() {
        // Arrange
        var view = new JsonWithCountParcelsView(new ParcelsMapperImpl());

        // Act
        var result = view.getOutputData(List.of());

        // Assert
        assertEquals("[]", result);
    }

    @Test
    void getOutputData_oneParcel_returnsParcelInfoInJson() {
        // Arrange
        var view = new JsonWithCountParcelsView(new ParcelsMapperImpl());
        var parcel = new Parcel(List.of(
                List.of('9', '9', '9'),
                List.of('9', '9', '9'),
                List.of('9', '9', '9')
        ));

        // Act
        var result = removeWhitespacesUsingGson(view.getOutputData(List.of(parcel)));

        // Assert
        assertEquals("[{\"parcel\":{\"width\":3,\"height\":3,\"form\":\"xxx\\nxxx\\nxxx\",\"dimensions\":9,\"symbol\":\"9\",\"name\":\"Посылка тип 9\"},\"quantity\":1}]", result);
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