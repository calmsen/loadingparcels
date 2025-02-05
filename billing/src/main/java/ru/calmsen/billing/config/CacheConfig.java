package ru.calmsen.billing.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("billings");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES) // TTL: записи живут 30 минут
                .expireAfterAccess(10, TimeUnit.MINUTES) // LRU: удаление неактивных данных через 10 минут
                .maximumSize(1000) // LFU: удаление редко используемых записей при переполнении
                .removalListener((key, value, cause) ->
                        System.out.println("Удалено: " + key + " по причине " + cause))
                .recordStats());
        return cacheManager;
    }
}
