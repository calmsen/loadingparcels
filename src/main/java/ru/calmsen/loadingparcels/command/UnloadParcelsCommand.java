package ru.calmsen.loadingparcels.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.model.domain.enums.OutputType;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.util.OutputDataWriterFactory;
import ru.calmsen.loadingparcels.view.TxtParcelsView;

@Slf4j
@RequiredArgsConstructor
public class UnloadParcelsCommand implements Command {
    private final ParcelsService parcelsService;
    private final TxtParcelsView txtParcelsView;
    private final OutputDataWriterFactory outputDataWriterFactory;

    @Override
    public boolean isMatch(String command) {
        return command.startsWith("unload");
    }

    @Override
    public void execute(CommandContext context) {
        var inputFileName = getInputFileName(context);
        var outputFileName = getOutputFileName(context);
        log.info("Начало разгрузки посылок из файла {}", inputFileName);
        var boxes = parcelsService.unloadTrucks(inputFileName);
        var output = txtParcelsView.getOutputData(boxes);
        writeOutputData(outputFileName, output);
        log.info("Разгрузка посылок из файла {} успешно завершена", inputFileName);
    }

    private void writeOutputData(String fileName, String output) {
        var outputType = fileName.isBlank() ? OutputType.CONSOLE : OutputType.FILE;
        outputDataWriterFactory.create(outputType, fileName).write(output);
    }

    private String getInputFileName(CommandContext context) {
        return context.getArgValue("unload", "trucks.json");
    }

    private String getOutputFileName(CommandContext context) {
        return context.getArgValue("--out", "parcels.txt");
    }
}
