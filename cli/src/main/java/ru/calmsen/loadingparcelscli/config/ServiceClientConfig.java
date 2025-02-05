package ru.calmsen.loadingparcelscli.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.calmsen.loadingparcelscli.service.BillingService;
import ru.calmsen.loadingparcelscli.service.LoadingParcelsService;

@Configuration
public class ServiceClientConfig {
    @Value("${services.url.loading-parcels-service}")
    private String loadingParcelsServiceUrl;

    @Value("${services.url.billing-service}")
    private String billingServiceUrl;

    @Bean
    public LoadingParcelsService loadingParcelsService() {
        return buildService(loadingParcelsServiceUrl, LoadingParcelsService.class);
    }

    @Bean
    public BillingService billingService() {
        return buildService(billingServiceUrl, BillingService.class);
    }

    private <T> T buildService(String url, Class<T> clazz) {
        var restClient = RestClient.builder()
                .baseUrl(url)
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(clazz);
    }
}