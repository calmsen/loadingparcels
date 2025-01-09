package ru.calmsen.loadingparcels.util;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.exception.FileWriterException;

import java.io.FileWriter;

/**
 * Запись данных в файл
 */
@RequiredArgsConstructor
public class FileOutputDataWriter implements OutputDataWriter {
    private final String fileName;

    /**
     * Записывает в файл данные
     *
     * @param data данные, необходимые для записи в файл
     */
    public void write(String data) {
        try (var fileWriter = new FileWriter(fileName)) {
            fileWriter.write(data);
        } catch (Exception e) {
            throw new FileWriterException("Не удалось записать в файл: " + fileName, e);
        }
    }
}
