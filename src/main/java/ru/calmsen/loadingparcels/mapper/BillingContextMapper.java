package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.calmsen.loadingparcels.command.impl.BillingCommand;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Маппер для контекста команды billing.
 */
@Mapper
public abstract class BillingContextMapper {

    @Mapping(target = "fromDate", source = "from")
    @Mapping(target = "toDate", source = "to")
    public abstract BillingCommand.Context toContext(Map<String, String> map);

    public LocalDate toDate(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
