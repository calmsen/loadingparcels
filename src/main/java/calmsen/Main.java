package calmsen;

import calmsen.controller.ParcelsController;
import calmsen.service.ParcelsService;
import calmsen.util.FileLinesReader;
import calmsen.util.parcelsparser.*;
import calmsen.view.TrucksView;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Стартуем приложение...");
        Main.start();
    }

    private static void start() {
        var parcelParsers = Arrays.asList(
                new TypeTwoParcelParser(),
                new TypeOneParcelParser(),
                new TypeThreeParcelParser(),
                new TypeFourParcelParser(),
                new TypeFiveParcelParser(),
                new TypeSixParcelParser(),
                new TypeSevenParcelParser(),
                new TypeEightParcelParser(),
                new TypeNineParcelParser());

        var consoleController = new ParcelsController(
                new ParcelsService(
                        new ParcelsParser(new FileLinesReader(), parcelParsers)
                ));
        consoleController.listen();
    }
}