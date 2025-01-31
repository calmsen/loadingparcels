package ru.calmsen.loadingparcelsbot.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

public interface LoadingParcelsService {
    @PostExchange("api/v1/commands/{commandName}")
    String send(
            @PathVariable("commandName") String commandName,
            @RequestBody Map<String, String> args);
}
