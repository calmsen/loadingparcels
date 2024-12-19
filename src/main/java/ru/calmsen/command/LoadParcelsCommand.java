package ru.calmsen.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.model.domain.enums.LoadingMode;
import ru.calmsen.service.ParcelsService;
import ru.calmsen.view.TrucksView;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class LoadParcelsCommand implements Command {
    private final ParcelsService parcelsService;
    private final TrucksView trucksView;
    private final Pattern LOAD_COMMAND_PATTERN = Pattern.compile("load (.+\\.txt)");

    @Override
    public boolean isMatch(String command) {
        Matcher matcher = LOAD_COMMAND_PATTERN.matcher(command);
        return matcher.matches();
    }

    @Override
    public void execute(CommandContext context) {
        Matcher matcher = LOAD_COMMAND_PATTERN.matcher(context.getCommand());
        if (matcher.matches()) {
            String filePath = matcher.group(1);

            var loadingMode = scannerLoadingMode(context.getScanner());
            processParcels(filePath, loadingMode);
        }
    }

    private void processParcels(String filePath, LoadingMode loadingMode) {
        log.info("Начало погрузки посылок из файла {}", filePath);
        var trucks = parcelsService.loadParcels(filePath, loadingMode);
        trucksView.showTrucks(trucks);
        log.info("Погрузка посылок из файла {} успешно завершена", filePath);
    }

    private LoadingMode scannerLoadingMode(Scanner scanner) {
        log.info("Выберете режим погрузки (simple|efficient) / по умолчанию simple");
        if (scanner.hasNextLine()) {
            String text = scanner.nextLine();
            var mode = LoadingMode.fromString(text);
            if (mode != null) {
                return mode;
            }
            log.error("Нет реализации для режима погрузки: {}. Выбран режим по умолчанию", text);
            return LoadingMode.SIMPLE;
        }
        return LoadingMode.SIMPLE;
    }
}
