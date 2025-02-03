package ru.calmsen.loadingparcelsbot.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import ru.calmsen.loadingparcelsbot.terminal.LoadingParcelsTelegramBot;

@Configuration
public class AppConfig {
    @Bean
    public ApplicationListener<ContextRefreshedEvent> LoadingParcelsTelegramBotRunner(LoadingParcelsTelegramBot loadingParcelsTelegramBot) {
        return event -> loadingParcelsTelegramBot.listen();
    }
}
