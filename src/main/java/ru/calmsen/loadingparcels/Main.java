package ru.calmsen.loadingparcels;

import ru.calmsen.loadingparcels.command.CommandProvider;
import ru.calmsen.loadingparcels.command.ExitCommand;
import ru.calmsen.loadingparcels.command.LoadParcelsCommand;
import ru.calmsen.loadingparcels.command.UnloadParcelsCommand;
import ru.calmsen.loadingparcels.controller.ParcelsController;
import ru.calmsen.loadingparcels.mapper.TrucksMapperImpl;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.service.parser.JsonTrucksParser;
import ru.calmsen.loadingparcels.util.ConsoleOutputDataWriter;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.service.parser.TxtParcelsParser;
import ru.calmsen.loadingparcels.util.OutputDataWriterFactory;
import ru.calmsen.loadingparcels.validator.ParcelValidator;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.view.TrucksViewFactory;
import ru.calmsen.loadingparcels.view.TxtParcelsView;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Стартуем приложение...");
        Main.start();
    }

    private static void start() {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        var fileReader = new FileReader();
        var outputDataWriterFactory = new OutputDataWriterFactory();

        var trucksMapper = new TrucksMapperImpl();

        var parcelsService = new ParcelsService(
                new TxtParcelsParser(fileReader),
                new JsonTrucksParser(fileReader, trucksMapper),
                new ParcelValidator(),
                new LoadingAlgorithmFactory()
        );
        var commands = List.of(
                new ExitCommand(),
                new LoadParcelsCommand(
                        parcelsService,
                        new TrucksViewFactory(trucksMapper),
                        outputDataWriterFactory
                ),
                new UnloadParcelsCommand(
                        parcelsService,
                        new TxtParcelsView(),
                        outputDataWriterFactory)
        );
        var commandProvider = new CommandProvider(commands);
        var consoleController = new ParcelsController(commandProvider, new ConsoleOutputDataWriter());
        consoleController.listen();
    }
}