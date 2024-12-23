package ru.calmsen.loadingparcels.util;

import ru.calmsen.loadingparcels.model.domain.enums.OutputType;

public class OutputDataWriterFactory {
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
