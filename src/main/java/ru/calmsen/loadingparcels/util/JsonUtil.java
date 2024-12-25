package ru.calmsen.loadingparcels.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        var gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(src);
    }
}
