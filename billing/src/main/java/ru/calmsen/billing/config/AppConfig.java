package ru.calmsen.billing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.calmsen.billing.model.domain.enums.ViewFormat;
import ru.calmsen.billing.view.BillingsView;
import ru.calmsen.billing.view.impl.*;

import java.time.Clock;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Map<ViewFormat, BillingsView> billingsView() {
        return Map.of(
                ViewFormat.TXT, new TxtBillingsView(),
                ViewFormat.JSON, new JsonBillingsView(),
                ViewFormat.CSV, new CsvBillingsView()
        );
    }
}
