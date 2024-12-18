package calmsen.controller;

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
public class ParcelsController {

    private final ParcelsService parcelsService;
    private final Pattern LOAD_COMMAND_PATTERN = Pattern.compile("load (.+\\.txt)");

    public void listen() {
        var scanner = new Scanner(System.in);

        log.info("Введите команду / по умолчанию load input.txt");
        while(scanner.hasNextLine()){
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                System.exit(0);
            }

            if (command.equals("load") || command.isBlank()) {
                command = "load input.txt";
            }

            Matcher matcher = LOAD_COMMAND_PATTERN.matcher(command);
            if (!matcher.matches()) {
                log.error("Нет реализации для команды: {}", command);
                continue;
            }

            var loadingMode = scannerLoadingMode(scanner);

            String filePath = matcher.group(1);
            tryLoadParcels(filePath, loadingMode);
            log.info("Введите команду / по умолчанию load input.txt");
        }
    }

    private void tryLoadParcels(String filePath, LoadingMode loadingMode) {
        log.info("Начало погрузки посылок из файла {}", filePath);
        try {
            var trucks = parcelsService.loadParcels(filePath, loadingMode);
            var trucksView = new TrucksView(trucks);
            trucksView.showTrucks();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.info("Погрузка посылок из файла {} завершена", filePath);
    }

    private static LoadingMode scannerLoadingMode(Scanner scanner) {
        log.info("Выберете режим погрузки (simple|efficient) / по умолчанию efficient");
        while(scanner.hasNextLine()){
            String mode = scanner.nextLine();
            if (mode.equals("exit")) {
                System.exit(0);
            }
            if (mode.equals("simple")) {
                return LoadingMode.SIMPLE;
            }
            if (mode.equals("efficient") || mode.isBlank()) {
                return LoadingMode.EFFICIENT;
            }

            log.error("Нет реализации для режима погрузки: {}", mode);
        }
        return LoadingMode.EFFICIENT;
    }

}
