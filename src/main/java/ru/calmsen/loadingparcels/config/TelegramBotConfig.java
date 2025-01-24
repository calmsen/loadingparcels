package ru.calmsen.loadingparcels.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class TelegramBotConfig {
    private String token;
    private String name;
}
