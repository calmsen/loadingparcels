package ru.calmsen.loadingparcels.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Класс для работы с json
 */
public class JsonUtil {
    /**
     * Десериализует json в объект
     *
     * @param json     строка
     * @param classOfT тип целевого объекта
     * @return целевой объект
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }

    /**
     * Сериализует объект в json
     * @param src объект, который надо сериализовать
     * @return json строка
     */
    public static String toJson(Object src) {
        var gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(src);
    }
}
