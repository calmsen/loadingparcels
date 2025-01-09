package ru.calmsen.loadingparcels.util;

import ru.calmsen.loadingparcels.model.domain.enums.OutputType;

/**
 * Фабрика создания объектов, служащих для вывода выходных данных.
 */
public class OutputDataWriterFactory {
    /**
     * Создает объект для вывода выходных данных
     *
     * @param type     тип объекта
     * @param fileName наименование файла
     * @return объект для вывода выходных данных
     */
    public OutputDataWriter create(OutputType type, String fileName) {
        switch (type) {
            case CONSOLE -> {
                return new ConsoleOutputDataWriter();
            }
            case FILE -> {
                return new FileOutputDataWriter(fileName);
            }
        }

        throw new IllegalArgumentException("Нет реализации OutputDataWriter с типом " + type);
    }
}
