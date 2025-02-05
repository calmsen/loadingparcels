package ru.calmsen.loadingparcelscli.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.calmsen.loadingparcelscli.command.CommandParameter;
import ru.calmsen.loadingparcelscli.service.BillingService;

import java.util.Map;

/**
 * Контроллер для работы со счетами
 */
@ShellComponent
@RequiredArgsConstructor
public class BillingsController {
    private final BillingService billingService;

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
            @ShellOption(value = CommandParameter.Billing.FROM, defaultValue = CommandParameter.Billing.FROM_DEFAULT_VALUE) String from,
            @ShellOption(value = CommandParameter.Billing.TO, defaultValue = CommandParameter.Billing.TO_DEFAULT_VALUE) String to,
            @ShellOption(value = CommandParameter.Billing.PERIOD, defaultValue = CommandParameter.Billing.PERIOD_DEFAULT_VALUE) String period,
            @ShellOption(value = CommandParameter.Billing.OUT_FORMAT, defaultValue = CommandParameter.Billing.OUT_FORMAT_DEFAULT_VALUE) String outFormat) {
        if (period.equals("NONE") && (from.isEmpty() || to.isEmpty())) {
            throw new IllegalArgumentException("Не заданы даты 'от' и 'до'");
        }

        return billingService.send("billing", Map.of(
            CommandParameter.USER, user,
            CommandParameter.Billing.FROM, from,
            CommandParameter.Billing.TO, to,
            CommandParameter.Billing.PERIOD, period,
            CommandParameter.Billing.OUT_FORMAT, outFormat
        ));
    }
}
