package ru.calmsen.loadingparcels.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.mapper.FindParcelContextMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.enums.OutputType;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.util.OutputDataWriterFactory;
import ru.calmsen.loadingparcels.view.factory.ParcelsViewFactory;

import java.util.List;

/**
 * Команда поиска посылок.
 */
@Slf4j
@RequiredArgsConstructor
public class FindParcelCommand extends Command<FindParcelCommand.Context> {
    private final ParcelsService parcelsService;
    private final ParcelsViewFactory parcelsViewFactory;
    private final OutputDataWriterFactory outputDataWriterFactory;
    private final FindParcelContextMapper contextMapper;

    @Override
    protected String getName() {
        return "find";
    }

    @Override
    protected void execute(Context context) {
        var logMessage = context.parcelName != null
                ? "Просмотр посылки " + context.parcelName
                : "Просмотр посылок";
        log.info(logMessage);

        var parcels = context.parcelName != null
                ? List.of(parcelsService.findParcel(context.parcelName))
                : parcelsService.findAllParcels();
        var output = getOutputData(context, parcels);
        writeOutputData(context.outFile, output);
    }

    @Override
    protected Context toContext(String command) {
        return contextMapper.toContext(toMap(command));
    }

    private String getOutputData(Context context, List<Parcel> parcels) {
        var viewFormat = ViewFormat.redefineFormat(context.outFile, context.viewFormat);
        return parcelsViewFactory.createView(viewFormat).getOutputData(parcels);
    }

    private void writeOutputData(String fileName, String output) {
        var outputType = fileName == null ? OutputType.CONSOLE : OutputType.FILE;
        outputDataWriterFactory.create(outputType, fileName).write(output);
    }

    @Getter
    @Setter
    public static class Context {
        private String parcelName;
        private String outFile;
        private ViewFormat viewFormat;
    }
}
