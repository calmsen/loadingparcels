package ru.calmsen.loadingparcels.command.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.command.Command;
import ru.calmsen.loadingparcels.mapper.UnloadParcelsContextMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.util.FileWriter;
import ru.calmsen.loadingparcels.view.ParcelsView;

import java.util.List;
import java.util.Map;

/**
 * Команда разгрузки машин.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UnloadParcelsCommand extends Command<UnloadParcelsCommand.Context> {
    private final ParcelsService parcelsService;
    private final Map<ViewFormat, ParcelsView> parcelsViews;
    private final Map<ViewFormat, ParcelsView> parcelsViewsWithCount;
    private final FileWriter fileWriter;
    private final UnloadParcelsContextMapper contextMapper;

    @Override
    protected String getName() {
        return "unload";
    }

    @Override
    public String execute(Context context) {
        log.info("Начало разгрузки посылок из файла {}", context.inFile);
        fillViewFormatIfEmpty(context);

        var parcels = parcelsService.unloadTrucks(context.user, context.inFile);
        var output = getOutputData(context, parcels);
        writeOutputData(context.outFile, output);
        log.info("Разгрузка посылок из файла {} успешно завершена", context.inFile);
        return output;
    }

    @Override
    protected Context toContext(Map<String, String> args) {
        return contextMapper.toContext(args);
    }

    private String getOutputData(Context context, List<Parcel> parcels) {
        var viewFormat = ViewFormat.redefineFormat(context.outFile, context.viewFormat);
        return context.withCount
                ? parcelsViewsWithCount.get(viewFormat).buildOutputData(parcels)
                : parcelsViews.get(viewFormat).buildOutputData(parcels);
    }

    private void fillViewFormatIfEmpty(UnloadParcelsCommand.Context context) {
        if (context.viewFormat == null) {
            context.viewFormat = ViewFormat.JSON;
        }
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
        private String user;
    }
}
