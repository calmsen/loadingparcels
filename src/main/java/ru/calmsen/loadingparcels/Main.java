package ru.calmsen.loadingparcels;

import org.mapstruct.factory.Mappers;
import ru.calmsen.loadingparcels.command.*;
import ru.calmsen.loadingparcels.controller.ParcelsController;
import ru.calmsen.loadingparcels.mapper.*;
import ru.calmsen.loadingparcels.repository.ParcelsRepository;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.service.parser.JsonTrucksParser;
import ru.calmsen.loadingparcels.service.parser.TxtParcelsParser;
import ru.calmsen.loadingparcels.terminal.LoadingParcelsConsole;
import ru.calmsen.loadingparcels.terminal.LoadingParcelsTgBot;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.util.FileWriter;
import ru.calmsen.loadingparcels.validator.ParcelValidator;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.view.factory.DefaultParcelsViewFactory;
import ru.calmsen.loadingparcels.view.factory.DefaultTrucksViewFactory;
import ru.calmsen.loadingparcels.view.factory.UnloadParcelsViewFactory;

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
        var fileWriter = new FileWriter();

        var trucksMapper = Mappers.getMapper(TrucksMapper.class);
        var parcelsMapper = Mappers.getMapper(ParcelsMapper.class);

        var defaultParcelsViewFactory = new DefaultParcelsViewFactory(parcelsMapper);

        var parcelsService = new ParcelsService(
                new TxtParcelsParser(fileReader),
                new JsonTrucksParser(fileReader, trucksMapper),
                new ParcelValidator(),
                new LoadingAlgorithmFactory(),
                new ParcelsRepository(),
                fileReader
        );
        var commands = List.of(
                new ExitCommand(),
                new LoadParcelsCommand(
                        parcelsService,
                        new DefaultTrucksViewFactory(trucksMapper),
                        fileWriter,
                        Mappers.getMapper(LoadParcelsContextMapper.class)
                ),
                new UnloadParcelsCommand(
                        parcelsService,
                        new UnloadParcelsViewFactory(defaultParcelsViewFactory, parcelsMapper),
                        fileWriter,
                        Mappers.getMapper(UnloadParcelsContextMapper.class)),
                new CreateParcelCommand(parcelsService, parcelsMapper),
                new UpdateParcelCommand(parcelsService, parcelsMapper),
                new DeleteParcelCommand(parcelsService),
                new FindParcelCommand(
                        parcelsService,
                        defaultParcelsViewFactory,
                        Mappers.getMapper(FindParcelContextMapper.class))
        );
        var commandProvider = new CommandProvider(commands);
        var parcelsController = new ParcelsController(commandProvider);

        parcelsService.initParcels("initial_parcels.txt");

        var tgBot = new LoadingParcelsTgBot(
                "7589989172:AAFzKmwuDHribOZ0V-uhwxNusIpSRmil5LA", // Перенести в конфиг файл
                "loadingparcels_tgbot", // Перенести в конфиг файл
                parcelsController
        );
        tgBot.listen();

        var console = new LoadingParcelsConsole(parcelsController);
        console.listen();
    }
}