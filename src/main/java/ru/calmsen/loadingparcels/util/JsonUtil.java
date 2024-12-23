package ru.calmsen.loadingparcels.util;

import com.google.gson.*;

public class JsonUtil {
    public static  <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException{
        return new Gson().fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        var gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(src);
    }
}
