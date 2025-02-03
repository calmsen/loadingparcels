package ru.calmsen.loadingparcelsbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.calmsen.loadingparcelsbot.service.LoadingParcelsService;

@Configuration
public class LoadingParcelsServiceConfig {
    @Value("${services.url.loading-parcels-service}")
    private String baseUrl;

    @Bean
    public LoadingParcelsService loadingParcelsService() {
        var cardService = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        var restClientAdapter = RestClientAdapter.create(cardService);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(LoadingParcelsService.class);
    }
}