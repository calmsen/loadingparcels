package ru.calmsen;

import ru.calmsen.command.CommandProvider;
import ru.calmsen.command.ExitCommand;
import ru.calmsen.command.LoadParcelsCommand;
import ru.calmsen.controller.ParcelsController;
import ru.calmsen.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.service.ParcelsService;
import ru.calmsen.util.ConsoleLinesWriter;
import ru.calmsen.util.FileLinesReader;
import ru.calmsen.service.parser.ParcelsParser;
import ru.calmsen.validator.ParcelValidator;
import ru.calmsen.view.TrucksView;
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