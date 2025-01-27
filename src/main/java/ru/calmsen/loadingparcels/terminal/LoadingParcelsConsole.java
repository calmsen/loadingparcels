package ru.calmsen.loadingparcels.terminal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.command.CommandSender;

import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadingParcelsConsole {
    private final CommandSender commandSender;

    /**
     * Слушает команды из командной строки.
     */
    public void listen() {
        var scanner = new Scanner(System.in);
        write("Введите команду / по умолчанию " + CommandSender.DEFAULT_COMMAND);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            write(commandSender.send(command));

            write("Введите команду / по умолчанию " + CommandSender.DEFAULT_COMMAND);
        }
    }

    private void write(String output){
        System.out.println(output);
    }
}
