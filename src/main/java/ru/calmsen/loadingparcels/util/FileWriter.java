package ru.calmsen.loadingparcels.util;

import ru.calmsen.loadingparcels.exception.FileWriterException;

/**
 * Запись данных в файл
 */

public class FileWriter {
    /**
     * Записывает в файл данные
     *
     * @param fileName наименование файла
     * @param data данные, необходимые для записи в файл
     */
    public void write(String fileName, String data) {
        try (var fileWriter = new java.io.FileWriter(fileName)) {
            fileWriter.write(data);
        } catch (Exception e) {
            throw new FileWriterException("Не удалось записать в файл: " + fileName, e);
        }
    }
}
