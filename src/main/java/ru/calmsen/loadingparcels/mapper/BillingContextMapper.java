package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.calmsen.loadingparcels.command.CommandParameter;
import ru.calmsen.loadingparcels.command.impl.BillingCommand;
import ru.calmsen.loadingparcels.util.DateUtil;

import java.time.LocalDate;
import java.util.Map;

/**
 * Маппер для контекста команды billing.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BillingContextMapper {

    @Mapping(target = "fromDate", source = CommandParameter.Billing.FROM)
    @Mapping(target = "toDate", source = CommandParameter.Billing.TO)
    public abstract BillingCommand.Context toContext(Map<String, String> map);

    public LocalDate toDate(String value) {
        return DateUtil.toDate(value);
    }
}
