package ru.calmsen.billing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.calmsen.billing.command.CommandParameter;
import ru.calmsen.billing.command.impl.BillingCommand;
import ru.calmsen.billing.model.domain.enums.Period;
import ru.calmsen.billing.model.domain.enums.ViewFormat;
import ru.calmsen.billing.util.DateUtil;

import java.time.LocalDate;
import java.util.Map;

/**
 * Маппер для контекста команды billing.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BillingContextMapper {

    @Mapping(target = "fromDate", source = CommandParameter.Billing.FROM)
    @Mapping(target = "toDate", source = CommandParameter.Billing.TO)
    @Mapping(target = "period", source = CommandParameter.Billing.PERIOD,
            defaultValue = CommandParameter.Billing.PERIOD_DEFAULT_VALUE)
    @Mapping(target = "viewFormat", source = CommandParameter.Billing.OUT_FORMAT,
            defaultValue = CommandParameter.Billing.OUT_FORMAT_DEFAULT_VALUE)
    public abstract BillingCommand.Context toContext(Map<String, String> map);

    public LocalDate toDate(String value) {
        return DateUtil.toDate(value);
    }

    public ViewFormat toViewFormat(String value) {
        return ViewFormat.fromString(value);
    }

    public Period toPeriod(String value) {
        return Period.fromString(value);
    }
}
