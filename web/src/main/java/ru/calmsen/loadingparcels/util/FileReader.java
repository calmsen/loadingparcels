package ru.calmsen.loadingparcels.util;

import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.exception.FileReaderException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * Чтение текстового файла
 */
@Component
public class FileReader {
    /**
     * Читает текстовые данные из файла
     *
     * @param fileName наименование файла
     * @return список строк с данными
     */
    public List<String> readAllLines(String fileName) {
        try {
            return Files.readAllLines(getFile(fileName).toPath());
        } catch (Exception e) {
            throw new FileReaderException("Не удалось прочитать файл: " + fileName, e);
        }
    }

    /**
     * Читает текстовые данные из ресурса
     *
     * @param resourceName наименование ресурса
     * @return список строк с данными
     */
    public List<String> readAllLinesFromResource(String resourceName) {
        return Arrays.stream(readStringFromResource(resourceName).split("\n")).toList();
    }

    /**
     * Читает текстовые данные из файла
     *
     * @param fileName наименование файла
     * @return текстовые данные из файла
     */
    public String readString(String fileName) {
        try {
            return Files.readString(getFile(fileName).toPath());
        } catch (Exception e) {
            throw new FileReaderException("Не удалось прочитать файл: " + fileName, e);
        }
    }

    /**
     * Читает текстовые данные из ресурса
     *
     * @param resourceName наименование ресурса
     * @return текстовые данные из ресурса
     */
    public String readStringFromResource(String resourceName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new FileReaderException("Ресурс не найден: " + resourceName);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileReaderException("Не удалось прочитать файл: " + resourceName, e);
        }
    }

    private File getFile(String fileName) {
        return new File(fileName);
    }
}
