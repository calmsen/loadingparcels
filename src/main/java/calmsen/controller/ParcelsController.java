package calmsen.controller;

import calmsen.command.CommandContext;
import calmsen.command.CommandProvider;
import calmsen.view.TrucksView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class ParcelsController {
    private static final String DEFAULT_COMMAND = "load input.txt";
    private final CommandProvider commandProvider;

    public void listen() {
        var scanner = new Scanner(System.in);

        log.info("Введите команду / по умолчанию {}", DEFAULT_COMMAND);
        while(scanner.hasNextLine()){
            String command = scanner.nextLine();
            if (command.isEmpty()) {
                command = DEFAULT_COMMAND;
            }

            var foundCommand = commandProvider.findCommand(command);
            if (foundCommand != null) {
                try {
                    foundCommand.execute(new CommandContext(command, scanner));
                } catch (Exception e) {
                    log.error("При выполнении команды {} произошла ошибка: {}\n;stackTrace{}", command, e.getMessage(), e.getStackTrace());
                }
            }

            log.info("Введите команду / по умолчанию {}", DEFAULT_COMMAND);
        }
    }
}
