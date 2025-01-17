package ru.calmsen.loadingparcels.view;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.mapper.ParcelsMapperImpl;
import ru.calmsen.loadingparcels.model.domain.Parcel;

import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonWithCountParcelsViewTest {
    @Test
    void buildOutputData_emptyParcels_returnsEmptyArrayInJson() {
        // Arrange
        var view = new JsonWithCountParcelsView(new ParcelsMapperImpl());

        // Act
        var result = view.buildOutputData(List.of());

        // Assert
        assertThat(result).isEqualTo("[]");
    }

    @Test
    void buildOutputData_oneParcel_returnsParcelInfoInJson() {
        // Arrange
        var view = new JsonWithCountParcelsView(new ParcelsMapperImpl());
        var parcel = new Parcel(List.of(
                List.of('9', '9', '9'),
                List.of('9', '9', '9'),
                List.of('9', '9', '9')
        ));

        // Act
        var result = removeWhitespacesUsingGson(view.buildOutputData(List.of(parcel)));

        // Assert
        assertThat(result).isEqualTo("[{\"parcel\":{\"width\":3,\"height\":3,\"form\":\"xxx\\nxxx\\nxxx\",\"dimensions\":9,\"symbol\":\"9\",\"name\":\"Посылка тип 9\"},\"quantity\":1}]");
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