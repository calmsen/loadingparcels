package ru.calmsen.loadingparcels.command.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.calmsen.loadingparcels.command.Command;
import ru.calmsen.loadingparcels.mapper.BillingContextMapper;
import ru.calmsen.loadingparcels.model.domain.Billing;
import ru.calmsen.loadingparcels.service.BillingsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Команда "Детали счета"
 */
@RequiredArgsConstructor
public class BillingCommand extends Command<BillingCommand.Context> {
    private final BillingContextMapper contextMapper;
    private final BillingsService billingsService;

    @Override
    protected String getName() {
        return "billing";
    }

    @Override
    public String execute(Context context) {
        var billings = billingsService.getBillings(context.user, context.fromDate, context.toDate);
        return getOutputData(billings);
    }

    @Override
    protected BillingCommand.Context toContext(Map<String, String> map) {
        return contextMapper.toContext(map);
    }

    private String getOutputData(List<Billing> billings) {
        return billings.stream()
                .map(Billing::getDescription)
                .collect(Collectors.joining("\n"));
    }

    @Getter
    @Setter
    @Builder
    public static class Context {
        private LocalDate fromDate;
        private LocalDate toDate;
        private String user;
    }
}
