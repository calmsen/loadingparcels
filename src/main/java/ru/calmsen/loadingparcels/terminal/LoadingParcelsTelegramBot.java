package ru.calmsen.loadingparcels.terminal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.calmsen.loadingparcels.command.CommandSender;
import ru.calmsen.loadingparcels.config.TelegramBotConfig;

@Slf4j
@Component
public class LoadingParcelsTelegramBot extends TelegramLongPollingBot {
    private final String botName;
    private final CommandSender commandSender;

    public LoadingParcelsTelegramBot(TelegramBotConfig config, CommandSender commandSender) {
        super(config.getToken());
        this.botName = config.getName();
        this.commandSender = commandSender;
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

            sendMessage(chatId, commandSender.send(message));
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
}