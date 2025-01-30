package ru.calmsen.loadingparcels.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import ru.calmsen.loadingparcels.mapper.*;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.service.loadingalgorithm.*;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.service.parser.TrucksParser;
import ru.calmsen.loadingparcels.service.parser.impl.JsonTrucksParser;
import ru.calmsen.loadingparcels.service.parser.impl.TxtParcelsParser;
import ru.calmsen.loadingparcels.terminal.LoadingParcelsTelegramBot;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.view.BillingsView;
import ru.calmsen.loadingparcels.view.ParcelsView;
import ru.calmsen.loadingparcels.view.TrucksView;
import ru.calmsen.loadingparcels.view.impl.*;

import java.time.Clock;
import java.util.Map;

@Configuration
public class AppConfig {
    @Value("${parcels.initial-parcels-file-name}")
    private String initialParcelsFileName;

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ParcelsParser parcelsParser(FileReader fileReader) {
        return new TxtParcelsParser(fileReader);
    }

    @Bean
    public TrucksParser trucksParser(FileReader fileReader, TrucksMapper trucksMapper) {
        return new JsonTrucksParser(fileReader, trucksMapper);
    }

    @Bean
    public Map<ViewFormat, BillingsView> billingsView() {
        return Map.of(
                ViewFormat.TXT, new TxtBillingsView(),
                ViewFormat.JSON, new JsonBillingsView(),
                ViewFormat.CSV, new TxtBillingsView()
        );
    }

    @Bean
    public Map<ViewFormat, ParcelsView> parcelsViews(ParcelsMapper parcelsMapper) {
        return Map.of(
            ViewFormat.TXT, new TxtParcelsView(),
            ViewFormat.JSON, new JsonParcelsView(parcelsMapper),
            ViewFormat.CSV, new CsvParcelsView()
        );
    }

    @Bean
    public Map<ViewFormat, ParcelsView> parcelsViewsWithCount(ParcelsMapper parcelsMapper) {
        return Map.of(
            ViewFormat.TXT, new TxtWithCountParcelsView(),
            ViewFormat.JSON, new JsonWithCountParcelsView(parcelsMapper),
            ViewFormat.CSV, new CsvWithCountParcelsView()
        );
    }

    @Bean
    public Map<ViewFormat, TrucksView> trucksViews(TrucksMapper trucksMapper) {
        return Map.of(
                ViewFormat.TXT, new TxtTrucksView(),
                ViewFormat.JSON, new JsonTrucksView(trucksMapper)
        );
    }

    @Bean
    public Map<LoadingMode, LoadingAlgorithm> loadingAlgorithms() {
        return Map.of(
            LoadingMode.ONEPARCEL, new OneParcelLoadingAlgorithm(),
            LoadingMode.SIMPLE, new SimpleLoadingAlgorithm(),
            LoadingMode.UNIFORM, new UniformLoadingAlgorithm(),
            LoadingMode.EFFICIENT, new EfficientLoadingAlgorithm()
        );
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> initParcels(ParcelsService parcelsService) {
        return event -> parcelsService.initParcels(initialParcelsFileName);
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> LoadingParcelsTelegramBotRunner(LoadingParcelsTelegramBot loadingParcelsTelegramBot) {
        return event -> loadingParcelsTelegramBot.listen();
    }
}
