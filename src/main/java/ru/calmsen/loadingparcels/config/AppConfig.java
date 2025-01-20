package ru.calmsen.loadingparcels.config;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import ru.calmsen.loadingparcels.command.CommandProvider;
import ru.calmsen.loadingparcels.command.CommandSender;
import ru.calmsen.loadingparcels.command.impl.*;
import ru.calmsen.loadingparcels.controller.BillingsController;
import ru.calmsen.loadingparcels.controller.ParcelsController;
import ru.calmsen.loadingparcels.mapper.*;
import ru.calmsen.loadingparcels.repository.BillingsRepository;
import ru.calmsen.loadingparcels.repository.ParcelsRepository;
import ru.calmsen.loadingparcels.service.BillingsService;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.service.parser.TrucksParser;
import ru.calmsen.loadingparcels.service.parser.impl.JsonTrucksParser;
import ru.calmsen.loadingparcels.service.parser.impl.TxtParcelsParser;
import ru.calmsen.loadingparcels.terminal.LoadingParcelsTgBot;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.util.FileWriter;
import ru.calmsen.loadingparcels.validator.ParcelValidator;
import ru.calmsen.loadingparcels.view.factory.impl.DefaultParcelsViewFactory;
import ru.calmsen.loadingparcels.view.factory.impl.DefaultTrucksViewFactory;
import ru.calmsen.loadingparcels.view.factory.impl.UnloadParcelsViewFactory;

import java.util.List;

@Configuration
public class AppConfig {
    @Value("${parcels.initial-parcels-file-name}")
    private String initialParcelsFileName;

    @Bean
    public FileReader fileReader() {
        return new FileReader();
    }

    @Bean
    public FileWriter fileWriter() {
        return new FileWriter();
    }

    @Bean
    public ParcelsParser parcelsParser() {
        return new TxtParcelsParser(fileReader());
    }

    @Bean
    public TrucksParser trucksParser() {
        return new JsonTrucksParser(fileReader(), trucksMapper());
    }

    @Bean
    public TrucksMapper trucksMapper() {
        return Mappers.getMapper(TrucksMapper.class);
    }

    @Bean
    public ParcelsMapper parcelsMapper() {
        return Mappers.getMapper(ParcelsMapper.class);
    }

    @Bean
    public LoadParcelsContextMapper loadParcelsContextMapper() {
        return Mappers.getMapper(LoadParcelsContextMapper.class);
    }

    @Bean
    public UnloadParcelsContextMapper unloadParcelsContextMapper() {
        return Mappers.getMapper(UnloadParcelsContextMapper.class);
    }

    @Bean
    public FindParcelContextMapper findParcelContextMapper() {
        return Mappers.getMapper(FindParcelContextMapper.class);
    }

    @Bean
    public BillingContextMapper billingContextMapper() {
        return Mappers.getMapper(BillingContextMapper.class);
    }

    @Bean
    public DefaultParcelsViewFactory defaultParcelsViewFactory() {
        return new DefaultParcelsViewFactory(parcelsMapper());
    }

    @Bean
    public DefaultTrucksViewFactory defaultTrucksViewFactory() {
        return new DefaultTrucksViewFactory(trucksMapper());
    }

    @Bean
    public UnloadParcelsViewFactory unloadParcelsViewFactory() {
        return new UnloadParcelsViewFactory(defaultParcelsViewFactory(), parcelsMapper());
    }

    @Bean
    public LoadingAlgorithmFactory loadingAlgorithmFactory() {
        return new LoadingAlgorithmFactory();
    }

    @Bean
    public ParcelValidator parcelValidator() {
        return new ParcelValidator();
    }

    @Bean
    public ExitCommand exitCommand() {
        return new ExitCommand();
    }

    @Bean
    public LoadParcelsCommand loadParcelsCommand(BillingsService billingsService) {
        return new LoadParcelsCommand(
                parcelsService(billingsService),
                defaultTrucksViewFactory(),
                fileWriter(),
                loadParcelsContextMapper()
        );
    }

    @Bean
    public UnloadParcelsCommand unloadParcelsCommand(BillingsService billingsService) {
        return new UnloadParcelsCommand(
                parcelsService(billingsService),
                unloadParcelsViewFactory(),
                fileWriter(),
                unloadParcelsContextMapper()
        );
    }

    @Bean
    public CreateParcelCommand createParcelCommand(BillingsService billingsService) {
        return new CreateParcelCommand(parcelsService(billingsService), parcelsMapper());
    }

    @Bean
    public UpdateParcelCommand updateParcelCommand(BillingsService billingsService) {
        return new UpdateParcelCommand(parcelsService(billingsService), parcelsMapper());
    }

    @Bean
    public DeleteParcelCommand deleteParcelCommand(BillingsService billingsService) {
        return new DeleteParcelCommand(parcelsService(billingsService));
    }

    @Bean
    public FindParcelCommand findParcelCommand(BillingsService billingsService){
        return new FindParcelCommand(
                parcelsService(billingsService),
                defaultParcelsViewFactory(),
                findParcelContextMapper()
        );
    }

    @Bean
    public BillingCommand billingCommand(BillingsService billingsService){
        return new BillingCommand(
                billingContextMapper(),
                billingsService);
    }

    @Bean
    public ParcelsRepository parcelsRepository() {
        return new ParcelsRepository();
    }

    @Bean
    public BillingsRepository billingsRepository() {
        return new BillingsRepository();
    }

    @Bean
    public ParcelsService parcelsService(BillingsService billingsService) {
        return new ParcelsService(
                parcelsParser(),
                trucksParser(),
                parcelValidator(),
                loadingAlgorithmFactory(),
                parcelsRepository(),
                fileReader(),
                billingsService
        );

    }

    @Bean
    public BillingsService billingsService(BillingConfig billingConfig) {
        return new BillingsService(billingConfig, billingsRepository());
    }

    @Bean
    public ParcelsController parcelsController(BillingsService billingsService) {
        return new ParcelsController(
                findParcelCommand(billingsService),
                createParcelCommand(billingsService),
                updateParcelCommand(billingsService),
                deleteParcelCommand(billingsService),
                loadParcelsCommand(billingsService),
                unloadParcelsCommand(billingsService)
        );
    }

    @Bean
    public BillingsController billingsController(BillingsService billingsService) {
        return new BillingsController(billingCommand(billingsService));
    }

    @Bean
    public CommandProvider commandProvider(BillingsService billingsService) {
        return new CommandProvider(List.of(
                exitCommand(),
                loadParcelsCommand(billingsService),
                unloadParcelsCommand(billingsService),
                createParcelCommand(billingsService),
                updateParcelCommand(billingsService),
                deleteParcelCommand(billingsService),
                findParcelCommand(billingsService),
                billingCommand(billingsService)
        ));
    }

    @Bean
    public CommandSender commandSender(BillingsService billingsService) {
        return new CommandSender(commandProvider(billingsService));
    }

    @Bean
    public LoadingParcelsTgBot loadingParcelsTgBot(TgBotConfig tgBotConfig, BillingsService billingsService) {
        return new LoadingParcelsTgBot(
                tgBotConfig.getToken(),
                tgBotConfig.getName(),
                commandSender(billingsService)
        );
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> initParcels(ParcelsService parcelsService) {
        return event -> parcelsService.initParcels(initialParcelsFileName);
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> LoadingParcelsTgBotRunner(LoadingParcelsTgBot loadingParcelsTgBot) {
        return event -> loadingParcelsTgBot.listen();
    }
}
