package ru.calmsen.loadingparcels.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.model.domain.enums.OutputType;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.util.OutputDataWriterFactory;
import ru.calmsen.loadingparcels.view.TrucksViewFactory;

@Slf4j
@RequiredArgsConstructor
public class LoadParcelsCommand implements Command {
    private final ParcelsService parcelsService;
    private final TrucksViewFactory trucksViewFactory;
    private final OutputDataWriterFactory outputDataWriterFactory;

    @Override
    public boolean isMatch(String command) {
        return command.startsWith("load");
    }

    @Override
    public void execute(CommandContext context) {
        var outputFileName = getOutputFileName(context);
        // переопределим параметр для формата по умолчанию
        var defaultFormat = outputFileName.isBlank() ? ViewFormat.TXT : ViewFormat.JSON;

        var inputFileName = getInputFileName(context);
        var loadingMode = getLoadingMode(context);
        var viewFormat = getViewFormat(context, defaultFormat);
        var trucksCount = getTrucksCount(context);


        log.info("Начало погрузки посылок из файла {}", inputFileName);
        var trucks = parcelsService.loadParcels(inputFileName, loadingMode, trucksCount);
        var output = trucksViewFactory.createView(viewFormat).getOutputData(trucks);
        writeOutputData(outputFileName, output);
        log.info("Погрузка посылок из файла {} успешно завершена", inputFileName);
    }

    private void writeOutputData(String fileName, String output) {
        var outputType = fileName.isBlank() ? OutputType.CONSOLE : OutputType.FILE;
        outputDataWriterFactory.create(outputType, fileName).write(output);
    }

    private String getInputFileName(CommandContext context) {
        return context.getArgValue("load", "input.txt");
    }

    private String getOutputFileName(CommandContext context) {
        return context.getArgValueIfFound("--out", "trucks.json");
    }

    private LoadingMode getLoadingMode(CommandContext context) {
        return LoadingMode.fromString(context.getArgValue("--mode", "onebox"));
    }

    private ViewFormat getViewFormat(CommandContext context, ViewFormat defaultFormat) {
        return ViewFormat.fromString(context.getArgValue("--format", defaultFormat.toString()));
    }

    private int getTrucksCount(CommandContext context) {
        var count = context.getArgValue("--count", "10");
        return Integer.parseInt(count);
    }
}
