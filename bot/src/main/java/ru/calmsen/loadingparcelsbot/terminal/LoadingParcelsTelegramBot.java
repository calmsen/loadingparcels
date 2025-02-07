package ru.calmsen.loadingparcelsbot.terminal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.calmsen.loadingparcelsbot.config.TelegramBotConfig;
import ru.calmsen.loadingparcelsbot.service.BillingService;
import ru.calmsen.loadingparcelsbot.service.LoadingParcelsService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LoadingParcelsTelegramBot extends TelegramLongPollingBot {
    private final String botName;
    private final LoadingParcelsService loadingParcelsService;
    private final BillingService billingService;

    public LoadingParcelsTelegramBot(TelegramBotConfig config, LoadingParcelsService loadingParcelsService, BillingService billingService) {
        super(config.getToken());
        this.botName = config.getName();
        this.loadingParcelsService = loadingParcelsService;
        this.billingService = billingService;
    }

    /**
     * Получает сообщения от бота
     *
     * @param update информация от бота
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            var commandName = message.split(" ")[0];
            var args = toMap(message);
            sendMessage(chatId, processCommand(commandName, args));
        }
    }

    /**
     * Получает имя бота
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return this.botName;
    }

    /**
     * Запускает слушателя бота
     */
    public void listen() {
        try {
            var botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            log.error("Произошла ошибка", e);
        }
    }

    private String processCommand(String commandName, Map<String, String> args) {
        return commandName.equals("billing")
                ? billingService.send(commandName, args)
                : loadingParcelsService.send(commandName, args);
    }

    /**
     * Отправляет сообщения боту
     *
     * @param chatId  идентификатор чата
     * @param message текст сообщения
     */
    private void sendMessage(long chatId, String message) {
        try {
            var sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.enableHtml(true);
            sendMessage.setText("<pre>" + message + "</pre>");
            executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Произошла ошибка", e);
        }
    }



    /**
     * Преобразует аргументы в словарь. Разделяет строку на пары key-value по разделителю --.
     * А затем разделяет key-value. Ключом является первое слово, остальное является значением.
     * Значение можно обрамлять двойными кавычками, но необязательно.
     *
     * @param args аргументы.
     * @return словарь аргументов команды
     */
    private Map<String, String> toMap(String args) {
        args = args.replaceAll("\\\\n", "\n");
        var commandParts = Arrays.stream(args.trim().split("--"))
                .filter(s -> !s.isEmpty())
                .toList();
        Map<String, String> map = new HashMap<>();
        for (var commandPart : commandParts) {
            if (commandPart.indexOf(' ') == -1) {
                continue;
            }

            var key = commandPart.substring(0, commandPart.indexOf(' '));
            var value = commandPart.substring(commandPart.indexOf(' ') + 1).trim();
            value = stripQuotes(value);
            if (key.isEmpty() || value.isEmpty()) {
                continue;
            }

            map.put(key, value);
        }

        return map;
    }

    private String stripQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }
}