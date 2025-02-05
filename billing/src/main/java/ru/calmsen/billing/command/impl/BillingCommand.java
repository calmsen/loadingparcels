package ru.calmsen.billing.command.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.calmsen.billing.command.Command;
import ru.calmsen.billing.mapper.BillingContextMapper;
import ru.calmsen.billing.model.domain.enums.Period;
import ru.calmsen.billing.model.domain.enums.ViewFormat;
import ru.calmsen.billing.service.BillingsService;
import ru.calmsen.billing.view.BillingsView;

import java.time.LocalDate;
import java.util.Map;

/**
 * Команда "Детали счета"
 */
@Component
@RequiredArgsConstructor
public class BillingCommand extends Command<BillingCommand.Context> {
    private final BillingContextMapper contextMapper;
    private final BillingsService billingsService;
    private final Map<ViewFormat, BillingsView> billingsViews;

    @Override
    protected String getName() {
        return "billing";
    }

    @Override
    public String execute(Context context) {
        var billings = context.period == Period.LAST_MONTH
            ? billingsService.findBillingsForLastMonth(context.user)
            : billingsService.findBillings(context.user, context.fromDate, context.toDate);
        return billingsViews.get(context.viewFormat).buildOutputData(billings);
    }

    @Override
    protected BillingCommand.Context toContext(Map<String, String> args) {
        return contextMapper.toContext(args);
    }

    @Getter
    @Setter
    @Builder
    public static class Context {
        private LocalDate fromDate;
        private LocalDate toDate;
        private Period period;
        private String user;
        private ViewFormat viewFormat;
    }
}
