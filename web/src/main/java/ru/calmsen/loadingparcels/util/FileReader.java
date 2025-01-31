package ru.calmsen.loadingparcels.util;

import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.exception.FileReaderException;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * Чтение текстового файла
 */
@Component
public class FileReader {
    /**
     * Читает текстовые данные из файла построчно
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

    private File getFile(String fileName) {
        var resource = getClass().getClassLoader().getResource(fileName);
        if (resource != null) {
            return new File(resource.getFile());
        }
        return new File(fileName);
    }
}
