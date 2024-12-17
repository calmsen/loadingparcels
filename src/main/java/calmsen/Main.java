package calmsen;

import calmsen.controller.ParcelsController;
import calmsen.service.ParcelsService;
import calmsen.util.FileLinesReader;
import calmsen.util.ParcelsParser;
import calmsen.validator.ParcelValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Стартуем приложение...");
        Main.start();
    }

    private static void start() {
        var consoleController = new ParcelsController(
                new ParcelsService(
                        new ParcelsParser(new FileLinesReader()),
                        new ParcelValidator()
                ));
        consoleController.listen();
    }
}