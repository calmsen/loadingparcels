package ru.calmsen.loadingparcels.util;

/**
 * Вывод выходных данных в консоль
 */
public class ConsoleOutputDataWriter implements OutputDataWriter {
    /**
     * Выводить в консоль данные
     *
     * @param data данные, необходимые для вывода в консоль
     */
    public void write(String data) {
        System.out.println(data);
    }
}
