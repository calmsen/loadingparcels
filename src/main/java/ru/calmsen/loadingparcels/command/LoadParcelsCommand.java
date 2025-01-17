package ru.calmsen.loadingparcels.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.mapper.LoadParcelsContextMapper;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.util.FileWriter;
import ru.calmsen.loadingparcels.view.factory.DefaultTrucksViewFactory;

import java.util.List;

/**
 * Команда погрузки машин.
 */
@Slf4j
@RequiredArgsConstructor
public class LoadParcelsCommand extends Command<LoadParcelsCommand.Context> {
    private final ParcelsService parcelsService;
    private final DefaultTrucksViewFactory trucksViewFactory;
    private final FileWriter fileWriter;
    private final LoadParcelsContextMapper contextMapper;

    @Override
    protected String getName() {
        return "load";
    }

    @Override
    protected String execute(Context context) {
        log.info("Начало погрузки посылок из файла");
        var trucks = loadTrucks(context);
        var output = getOutputData(context, filterEmptyTrucks(trucks));
        writeOutputData(context.outFile, output);
        log.info("Погрузка посылок успешно завершена");
        return output;
    }

    @Override
    protected Context toContext(String command) {
        return contextMapper.toContext(toMap(command));
    }

    private List<Truck> filterEmptyTrucks(List<Truck> trucks) {
        return trucks.stream().filter(x -> !x.isEmpty()).toList();
    }

    private List<Truck> loadTrucks(Context context) {
        if (context.trucks == null || context.trucks.isEmpty()) {
            return List.of();
        }

        if (context.parcelNames != null && !context.parcelNames.isEmpty()) {
            parcelsService.loadParcels(context.parcelNames, context.loadingMode, context.trucks);
        }
        else {
            parcelsService.loadParcels(context.inFile, context.loadingMode, context.trucks);
        }

        return context.trucks;
    }

    private String getOutputData(Context context, List<Truck> trucks) {
        var viewFormat = ViewFormat.redefineFormat(context.outFile, context.viewFormat);
        return trucksViewFactory.createView(viewFormat).buildOutputData(trucks);
    }

    private void writeOutputData(String fileName, String output) {
        if (fileName == null) {
            return;
        }

        fileWriter.write(fileName, output);
    }

    @Getter
    @Setter
    public static class Context {
        private String inFile;
        private String outFile;
        private LoadingMode loadingMode;
        private ViewFormat viewFormat;
        private List<Truck> trucks;
        private List<String> parcelNames;
    }
}
