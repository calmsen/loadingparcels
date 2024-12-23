package ru.calmsen.loadingparcels.util;

public class ConsoleOutputDataWriter implements OutputDataWriter {
    public void write(String data) {
        System.out.println(data);
    }
}
