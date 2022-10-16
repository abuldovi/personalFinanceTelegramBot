package abuldovi.telegram.telegramApp.handlers;

import abuldovi.telegram.telegramApp.enums.BotState;
import abuldovi.telegram.telegramApp.models.Transaction;
import abuldovi.telegram.telegramApp.service.TransactionService;
import abuldovi.telegram.telegramApp.util.BotStateMenu;
import abuldovi.telegram.telegramApp.util.TransactionState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

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

    public SendMessage addValue(long chatId, Message message){


            Transaction transaction = new Transaction();
            transaction.setValue(Integer.parseInt(message.getText()));
            transactionState.changeTransactionState(chatId, transaction);


        return showCategory(chatId);
    }

    public EditMessageText addExpenses(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.ADDVALUE);

        return editMessageTextBuilder(chatId, messageId, "How much have you spent?", keyboards.getHomeKeyboard());

    }

    public EditMessageText showSource(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.ADDSOURCE);

        return editMessageTextBuilder(chatId, messageId, "Choose the category", keyboards.getSourceKeyboard());


    }

    public EditMessageText addSource(long chatId, int messageId) {

        Transaction transaction = transactionState.getTransactionState(chatId);

        transactionService.save(chatId, transaction);

        transactionState.removeTransactionState(chatId);

        EditMessageText editMessageText = startEdit(chatId, messageId);

        editMessageText.setText("Transaction successfully added");

        return editMessageText;
    }

    public EditMessageText startEdit(long chatId, int messageId) {

        EditMessageText message = editMessageTextBuilder(chatId, messageId, "Choose the option", keyboards.getStartKeyboard());

        botStateMenu.changeBotState(chatId, BotState.START);

        return message;

    }

    public SendMessage showCategory(long chatId) {

        botStateMenu.changeBotState(chatId, BotState.ADDCATEGORY);

        return SendMessage.builder().chatId(String.valueOf(chatId)).replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getCategoryKeyboard()).build()).text("Choose the category").build();

    }

    public EditMessageText editMessageTextBuilder(long chatId, int messageId, String text, List<List<InlineKeyboardButton>> keyboard) {

        return EditMessageText.builder().chatId(String.valueOf(chatId)).messageId(messageId).text(text).replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboard).build()).build();

    }


}
