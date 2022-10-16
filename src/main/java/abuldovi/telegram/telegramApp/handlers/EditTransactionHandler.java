package abuldovi.telegram.telegramApp.handlers;

import abuldovi.telegram.telegramApp.enums.BotState;
import abuldovi.telegram.telegramApp.exception.TransactionNullValueException;
import abuldovi.telegram.telegramApp.models.Transaction;
import abuldovi.telegram.telegramApp.service.TransactionService;
import abuldovi.telegram.telegramApp.util.BotStateMenu;
import abuldovi.telegram.telegramApp.util.TransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.PatternSyntaxException;

@Component
public class EditTransactionHandler {
    private final TransactionService transactionService;
    private final TransactionState transactionState;
    private final BotStateMenu botStateMenu;
    private final Keyboards keyboards;
    private final ShowTransactionsHandler showTransactionsHandler;

    @Autowired
    public EditTransactionHandler(TransactionService transactionService, TransactionState transactionState, BotStateMenu botStateMenu, Keyboards keyboards, ShowTransactionsHandler showTransactionsHandler) {
        this.transactionService = transactionService;
        this.transactionState = transactionState;
        this.botStateMenu = botStateMenu;
        this.keyboards = keyboards;
        this.showTransactionsHandler = showTransactionsHandler;
    }


    public SendMessage findTransaction(Message message) {

        long chatId = message.getChatId();

        Transaction transaction = transactionService.findByTransactionId(Integer.parseInt(message.getText()), chatId);

        if (transaction == null) {
            throw new TransactionNullValueException("Incorrect Transaction ID");
        }

        transactionState.changeTransactionState(chatId, transaction);

        botStateMenu.changeBotState(chatId, BotState.EDITTRANSACTION);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(keyboards.getEditKeyboard()).build())
                .text("What to change: \n\n" + showTransactionsHandler.transactionStringBuilder(transaction))
                .build();

    }

    public SendMessage addValue(Message message) {
        long chatId = message.getChatId();

        Transaction transaction = transactionState.getTransactionState(chatId);
        transaction.setValue(Integer.parseInt(message.getText()));
        transactionService.save(transaction);

        botStateMenu.changeBotState(chatId, BotState.EDITTRANSACTION);

        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(keyboards.getEditKeyboard()).build())
                .text("Value successfully changed \n\n" + showTransactionsHandler.transactionStringBuilder(transaction))
                .build();
    }

    public SendMessage addDate(Message message) {

        String date = message.getText();

        if(!date.matches("[0-3][0-9]\\/[0-1][0-9]\\/[2][0][0-9][0-9]")){
                throw new PatternSyntaxException("Wrong date pattern, should be dd/mm/yyyy", "", -1);
        }

        long chatId = message.getChatId();

        date +=" 00:00:00";

        LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        Transaction transaction = transactionState.getTransactionState(chatId);
        transaction.setTimestamp(localDateTime);
        transactionService.save(transaction);

        botStateMenu.changeBotState(chatId, BotState.EDITTRANSACTION);

        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(keyboards.getEditKeyboard()).build())
                .text("Date has successfully changed \n\n" + showTransactionsHandler.transactionStringBuilder(transaction))
                .build();
    }

    public EditMessageText editExpense(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.EDITEXPENSEADDVALUE);

        return editMessageTextBuilder(chatId, messageId, "Please enter the transaction id", keyboards.getHomeKeyboard());

    }


    public EditMessageText editExpenseMenu(long chatId, int messageId, String resultMessage) {

        botStateMenu.changeBotState(chatId, BotState.EDITTRANSACTION);

        return editMessageTextBuilder(chatId, messageId, resultMessage, keyboards.getEditKeyboard());
    }


    public EditMessageText editValue(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.ADDVALUEEDITTRANSACTION);
        String value = String.valueOf(transactionState.getTransactionState(chatId).getValue());

        return editMessageTextBuilder(chatId, messageId, "Current value " + value + "\n\nType the new value:", keyboards.getHomeKeyboard());
    }


    public EditMessageText editCategory(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.CHOOSECATEGORYEDITTRANSACTION);

        return editMessageTextBuilder(chatId, messageId, "Choose category \n Current state:" + transactionState.getTransactionState(chatId).getCategory(), keyboards.getCategoryKeyboard());
    }

    public EditMessageText addCategory(long chatId, int messageId, String category) {

        botStateMenu.changeBotState(chatId, BotState.EDITTRANSACTION);

        Transaction transaction = transactionState.getTransactionState(chatId);
        transaction.setCategory(category);
        transactionService.save(chatId, transaction);

        return editExpenseMenu(chatId, messageId, "Category has successfully changed \n\n" + showTransactionsHandler.transactionStringBuilder(transaction));
    }


    public EditMessageText editSource(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.CHOOSESOURCEEDITTRANSACTION);

        return editMessageTextBuilder(chatId, messageId, "Choose source \n Current state:" + transactionState.getTransactionState(chatId).getSource(), keyboards.getSourceKeyboard());
    }

    public EditMessageText addSource(long chatId, int messageId, String source) {

        botStateMenu.changeBotState(chatId, BotState.EDITTRANSACTION);

        Transaction transaction = transactionState.getTransactionState(chatId);
        transaction.setSource(source);
        transactionService.save(chatId, transaction);

        return editExpenseMenu(chatId, messageId, "Source has successfully changed \n\n" + showTransactionsHandler.transactionStringBuilder(transaction));
    }

    public EditMessageText editDate(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.EDITDATEEDITTRANSACTION);

        String timestamp = transactionState.getTransactionState(chatId).getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return editMessageTextBuilder(chatId, messageId, "Current value: " + timestamp + "\n\n Type your date in a form dd/mm/yyyy", keyboards.getHomeKeyboard());
    }


    public EditMessageText editMessageTextBuilder(long chatId, int messageId, String text, List<List<InlineKeyboardButton>> keyboard) {

        return EditMessageText.builder().chatId(String.valueOf(chatId)).messageId(messageId).text(text).replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboard).build()).build();

    }


}
