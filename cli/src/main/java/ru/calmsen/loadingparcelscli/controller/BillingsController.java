package ru.calmsen.loadingparcelscli.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.calmsen.loadingparcelscli.command.CommandParameter;
import ru.calmsen.loadingparcelscli.service.LoadingParcelsService;

import java.util.Map;

/**
 * Контроллер для работы со счетами
 */
@ShellComponent
@RequiredArgsConstructor
public class BillingsController {
    private final LoadingParcelsService loadingParcelsService;

    /**
     * Детали счета
     *
     * @param user идентификатор пользователя
     * @param from дата от (включительно)
     * @param to   дата до (включительно)
     * @param outFormat    формат вывода
     * @return детали счета
     */
    @ShellMethod("Детали счета")
    public String billing(
            String user,
            String from,
            String to,
            @ShellOption(value = CommandParameter.Billing.OUT_FORMAT, defaultValue = CommandParameter.Billing.OUT_FORMAT_DEFAULT_VALUE) String outFormat) {
        return loadingParcelsService.send("billing", Map.of(
            CommandParameter.USER, user,
            CommandParameter.Billing.FROM, from,
            CommandParameter.Billing.TO, to,
            CommandParameter.Billing.OUT_FORMAT, outFormat
        ));
    }
}
