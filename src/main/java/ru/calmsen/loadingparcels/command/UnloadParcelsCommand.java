package ru.calmsen.loadingparcels.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.mapper.UnloadParcelsContextMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.enums.OutputType;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.util.FileWriter;
import ru.calmsen.loadingparcels.view.factory.ParcelsViewFactory;

import java.util.List;

/**
 * Команда разгрузки машин.
 */
@Slf4j
@RequiredArgsConstructor
public class UnloadParcelsCommand extends Command<UnloadParcelsCommand.Context> {
    private final ParcelsService parcelsService;
    private final ParcelsViewFactory parcelsViewFactory;
    private final FileWriter fileWriter;
    private final UnloadParcelsContextMapper contextMapper;

    @Override
    protected String getName() {
        return "unload";
    }

    @Override
    protected String execute(Context context) {
        log.info("Начало разгрузки посылок из файла {}", context.inFile);
        var parcels = parcelsService.unloadTrucks(context.inFile);
        var output = getOutputData(context, parcels);
        writeOutputData(context.outFile, output);
        log.info("Разгрузка посылок из файла {} успешно завершена", context.inFile);
        return output;
    }

    @Override
    protected Context toContext(String command) {
        return contextMapper.toContext(toMap(command));
    }

    private String getOutputData(Context context, List<Parcel> parcels) {
        var viewFormat = ViewFormat.redefineFormat(context.outFile, context.viewFormat);
        var factoryContext = ParcelsViewFactory.Context.builder()
                .withCount(context.withCount)
                .build();
        return parcelsViewFactory.createView(viewFormat, factoryContext).getOutputData(parcels);
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
        private ViewFormat viewFormat;
        private boolean withCount;
    }
}
