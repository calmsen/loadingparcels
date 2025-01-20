package ru.calmsen.loadingparcels.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "tgbot")
public class TgBotConfig {
    private String token;
    private String name;
}
