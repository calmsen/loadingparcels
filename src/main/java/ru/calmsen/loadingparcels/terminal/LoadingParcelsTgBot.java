package ru.calmsen.loadingparcels.terminal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.calmsen.loadingparcels.controller.ParcelsController;

@Slf4j
@RequiredArgsConstructor
public class LoadingParcelsTgBot extends TelegramLongPollingBot {
    private final String botName;
    private ParcelsController parcelsController;

    public LoadingParcelsTgBot(String botToken, String botName, ParcelsController parcelsController) {
        super(botToken);
        this.botName = botName;
        this.parcelsController = parcelsController;
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

            var result = parcelsController.handleCommand(message);
            if (result.data() != null) {
                sendMessage(chatId, result.data());
            }

            if (result.error() != null) {
                sendMessage(chatId, result.error());
            }
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