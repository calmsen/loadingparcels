package ru.calmsen.billing.mapper;

import lombok.Setter;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.model.dto.ParcelsBillingDto;
import ru.calmsen.billing.util.DateUtil;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Setter
public abstract class BillingMapper {
    @Autowired
    protected Clock clock;

    @Mapping(target = "type", source = "operationType")
    @Mapping(target = "quantity", source = "filledPlaces")
    public abstract Billing toBilling(ParcelsBillingDto message, @Context BigDecimal cost);

    @AfterMapping
    public Billing toBillingAfterMapping(ParcelsBillingDto message, @MappingTarget Billing billing, @Context BigDecimal cost) {
        billing.setDate(LocalDate.now(clock));
        billing.setCost(cost);
        billing.setDescription(toDescription(billing, message.getTrucksCount(), message.getParcelsCount()));
        return billing;
    }

    private String toDescription(Billing billing, int trucksCount, int parcelsCount) {
        return String.format(
                Billing.DescriptionFormat,
                DateUtil.toString(billing.getDate()),
                billing.getType(),
                trucksCount,
                parcelsCount,
                billing.getCost()
        );
    }
}
