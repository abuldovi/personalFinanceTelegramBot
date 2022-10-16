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

import java.util.List;

@Component
public class DeleteTransactionHandler {

        private final TransactionService transactionService;
        private final TransactionState transactionState;
        private final BotStateMenu botStateMenu;
        private final Keyboards keyboards;
        private final ShowTransactionsHandler showTransactionsHandler;

        @Autowired
        public DeleteTransactionHandler(TransactionService transactionService, TransactionState transactionState, BotStateMenu botStateMenu, Keyboards keyboards, ShowTransactionsHandler showTransactionsHandler) {
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

        botStateMenu.changeBotState(chatId, BotState.DELETETRANSACTION);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(keyboards.getDeleteKeyboard()).build())
                .text("Do you want to delete this transaction?: \n\n" + showTransactionsHandler.transactionStringBuilder(transaction))
                .build();

    }

    public EditMessageText deleteStart(long chatId, int messageId) {


        botStateMenu.changeBotState(chatId, BotState.DELETETRANSACTION);

        return editMessageTextBuilder(chatId, messageId, "Type the id of transaction you want to delete", keyboards.getHomeKeyboard());

    }

    public EditMessageText deleteMessageFinalYes(long chatId, int messageId) {
            botStateMenu.changeBotState(chatId, BotState.DELETETRANSACTIONSTEP2);
            return editMessageTextBuilder(chatId, messageId, "Are you sure?", keyboards.getDeleteKeyboard());
    }

    public EditMessageText deleteTransaction(long chatId, int messageId) {
            Transaction transaction = transactionState.getTransactionState(chatId);
            transactionService.deleteTransaction(transaction);
        botStateMenu.changeBotState(chatId, BotState.START);

        transactionState.removeTransactionState(chatId);
        return editMessageTextBuilder(chatId, messageId, "Transaction has been successfully deleted!", keyboards.getStartKeyboard());
    }

    public EditMessageText deleteMessageFinalNo(long chatId, int messageId) {
            botStateMenu.changeBotState(chatId, BotState.START);
        transactionState.removeTransactionState(chatId);
            return editMessageTextBuilder(chatId, messageId, "Choose your option", keyboards.getStartKeyboard());
    }


    public EditMessageText editMessageTextBuilder(long chatId, int messageId, String text, List<List<InlineKeyboardButton>> keyboard) {

        return EditMessageText.builder().chatId(String.valueOf(chatId)).messageId(messageId).text(text).replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboard).build()).build();

    }
}


