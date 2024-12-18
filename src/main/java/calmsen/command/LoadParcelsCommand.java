package calmsen.command;

import calmsen.model.domain.enums.LoadingMode;
import calmsen.service.ParcelsService;
import calmsen.view.TrucksView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class LoadParcelsCommand extends Command {
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
            tryLoadParcels(filePath, loadingMode);
        }

    }

    private void tryLoadParcels(String filePath, LoadingMode loadingMode) {
        log.info("Начало погрузки посылок из файла {}", filePath);
        try {
            var trucks = parcelsService.loadParcels(filePath, loadingMode);
            trucksView.showTrucks(trucks);
            log.info("Погрузка посылок из файла {} успешно завершена", filePath);
        } catch (Exception e) {
            log.error("Погрузка посылок из файла {} завершена с ошибкой", filePath);
            throw e;
        }


    }

    private static LoadingMode scannerLoadingMode(Scanner scanner) {
        log.info("Выберете режим погрузки (simple|efficient) / по умолчанию simple");
        if (scanner.hasNextLine()){
            String mode = scanner.nextLine();
            if (mode.equals("simple")) {
                return LoadingMode.SIMPLE;
            }
            if (mode.equals("efficient")) {
                return LoadingMode.EFFICIENT;
            }

            log.error("Нет реализации для режима погрузки: {}. Выбран режим по умолчанию", mode);
        }
        return LoadingMode.SIMPLE;
    }
}
