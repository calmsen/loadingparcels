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
import ru.calmsen.loadingparcels.util.ConsoleOutputDataWriter;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.util.OutputDataWriterFactory;
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
        var outputDataWriterFactory = new OutputDataWriterFactory();

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
                        outputDataWriterFactory,
                        Mappers.getMapper(LoadParcelsContextMapper.class)
                ),
                new UnloadParcelsCommand(
                        parcelsService,
                        new UnloadParcelsViewFactory(defaultParcelsViewFactory, parcelsMapper),
                        outputDataWriterFactory,
                        Mappers.getMapper(UnloadParcelsContextMapper.class)),
                new CreateParcelCommand(parcelsService, parcelsMapper),
                new UpdateParcelCommand(parcelsService, parcelsMapper),
                new DeleteParcelCommand(parcelsService),
                new FindParcelCommand(
                        parcelsService,
                        defaultParcelsViewFactory,
                        outputDataWriterFactory,
                        Mappers.getMapper(FindParcelContextMapper.class))
        );
        var commandProvider = new CommandProvider(commands);
        var consoleController = new ParcelsController(commandProvider, new ConsoleOutputDataWriter());

        parcelsService.initParcels("initial_parcels.txt");

        consoleController.listen();
    }
}