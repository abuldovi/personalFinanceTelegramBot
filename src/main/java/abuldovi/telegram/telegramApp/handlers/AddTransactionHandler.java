package abuldovi.telegram.telegramApp.handlers;

import abuldovi.telegram.telegramApp.enums.BotState;
import abuldovi.telegram.telegramApp.models.Transaction;
import abuldovi.telegram.telegramApp.service.TransactionService;
import abuldovi.telegram.telegramApp.util.BotStateMenu;
import abuldovi.telegram.telegramApp.util.TransactionState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class AddTransactionHandler {

    private final TransactionService transactionService;
    private final TransactionState transactionState;
    private final BotStateMenu botStateMenu;
    private final Keyboards keyboards;

    public AddTransactionHandler(TransactionService transactionService, TransactionState transactionState, BotStateMenu botStateMenu, Keyboards keyboards) {
        this.transactionService = transactionService;
        this.transactionState = transactionState;
        this.botStateMenu = botStateMenu;
        this.keyboards = keyboards;
    }


    public EditMessageText addExpenses(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.ADDVALUE);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getHomeKeyboard()).build())
                .text("Please enter the number")
                .build();

    }

    public EditMessageText showSource(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.ADDSOURCE);

        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(String.valueOf(chatId))
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getSourceKeyboard()).build())
                .text("Choose the category")
                .build();


    }

    public EditMessageText addSource(long chatId, int messageId) {

        Transaction transaction = transactionState.getTransactionState(chatId);

        transactionService.save(chatId, transaction);

        transactionState.removeTransactionState(chatId);

        EditMessageText editMessageText = startEdit(chatId, messageId);

        editMessageText.setText("Transaction successfully added");

        return  editMessageText;
    }

    public EditMessageText startEdit(long chatId, int messageId) {

        EditMessageText message = EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text("Choose the option")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getStartKeyboard()).build())
                .build();

        botStateMenu.changeBotState(chatId, BotState.START);

        return message;

    }


}
