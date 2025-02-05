package ru.calmsen.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import ru.calmsen.billing.model.dto.LoadParcelsBillingDto;
import ru.calmsen.billing.service.BillingsService;

import java.util.function.Consumer;

@Configuration
public class ConsumerConfig {
    @Bean
    public Consumer<Message<LoadParcelsBillingDto>> loadParcelsBilling(BillingsService billingsService) {
        return message -> billingsService.addLoadParcelsBilling(message.getPayload());
    }

    @Bean
    public Consumer<Message<LoadParcelsBillingDto>> unloadParcelsBilling(BillingsService billingsService) {
        return message -> billingsService.addUnloadParcelsBilling(message.getPayload());
    }
}
