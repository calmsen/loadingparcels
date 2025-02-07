package ru.calmsen.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import ru.calmsen.billing.model.dto.ParcelsBillingDto;
import ru.calmsen.billing.service.BillingsService;

import java.util.function.Consumer;

@Configuration
public class ConsumerConfig {
    @Bean
    public Consumer<Message<ParcelsBillingDto>> addParcelsBilling(BillingsService billingsService) {
        return message -> billingsService.addParcelsBilling(message.getPayload());
    }
}
