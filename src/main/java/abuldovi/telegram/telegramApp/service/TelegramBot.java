package abuldovi.telegram.telegramApp.service;

import abuldovi.telegram.telegramApp.config.BotConfig;
import abuldovi.telegram.telegramApp.handlers.MenuHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;
    final MenuHandler menuHandler;

    @Autowired
    public TelegramBot(BotConfig botConfig, MenuHandler menuHandler) {
        this.botConfig = botConfig;
        this.menuHandler = menuHandler;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update){

        if(update.hasMessage() && update.getMessage().hasEntities()) {
            try {
                handleCommand(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if(update.hasMessage()) {
            try {
                execute(menuHandler.handleMessage(update.getMessage()));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        if(update.hasCallbackQuery()) {
            try {
                execute(menuHandler.handleCallback(update.getCallbackQuery()));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }




    private void handleCommand(Message message) throws TelegramApiException {
            message.getChatId();
            Optional<MessageEntity> messageEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if(messageEntity.isPresent()){
                String command = message.getText().substring(messageEntity.get().getOffset(), messageEntity.get().getLength());
                switch (command){
                    case "/set_finance":
                        System.out.println(message.getChatId());
                        execute(menuHandler.start(message.getChatId()));
                        break;
                    default:
                        menuHandler.handleMessage(message);

                }

            }

        }












}
