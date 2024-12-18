package calmsen;

import calmsen.command.CommandProvider;
import calmsen.command.ExitCommand;
import calmsen.command.LoadParcelsCommand;
import calmsen.controller.ParcelsController;
import calmsen.loadingalgorithm.LoadingAlgorithmFactory;
import calmsen.service.ParcelsService;
import calmsen.util.ConsoleLinesWriter;
import calmsen.util.FileLinesReader;
import calmsen.parser.ParcelsParser;
import calmsen.validator.ParcelValidator;
import calmsen.view.TrucksView;
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
                new LoadParcelsCommand(new ParcelsService(
                        new ParcelsParser(new FileLinesReader()),
                        new ParcelValidator(),
                        new LoadingAlgorithmFactory()),
                        new TrucksView(new ConsoleLinesWriter())
                )
        );
        var commandProvider = new CommandProvider(commands);
        var consoleController = new ParcelsController(commandProvider);
        consoleController.listen();
    }
}