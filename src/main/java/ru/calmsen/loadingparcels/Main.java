package ru.calmsen.loadingparcels;

import ru.calmsen.loadingparcels.command.CommandProvider;
import ru.calmsen.loadingparcels.command.ExitCommand;
import ru.calmsen.loadingparcels.command.LoadParcelsCommand;
import ru.calmsen.loadingparcels.controller.ParcelsController;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.util.ConsoleLinesWriter;
import ru.calmsen.loadingparcels.util.FileLinesReader;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.validator.ParcelValidator;
import ru.calmsen.loadingparcels.view.TrucksView;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Стартуем приложение...");
        Main.start();
    }

    private static void start() {
        var commands = List.of(
                new ExitCommand(),
                new LoadParcelsCommand(
                        new ParcelsService(
                            new ParcelsParser(new FileLinesReader()),
                            new ParcelValidator(),
                            new LoadingAlgorithmFactory()
                        ),
                        new TrucksView(new ConsoleLinesWriter())
                )
        );
        var commandProvider = new CommandProvider(commands);
        var consoleController = new ParcelsController(commandProvider);
        consoleController.listen();
    }
}