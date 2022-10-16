package abuldovi.telegram.telegramApp.handlers;

import abuldovi.telegram.telegramApp.enums.BotState;
import abuldovi.telegram.telegramApp.exception.TransactionNullValueException;
import abuldovi.telegram.telegramApp.models.Transaction;
import abuldovi.telegram.telegramApp.util.BotStateMenu;
import abuldovi.telegram.telegramApp.util.RequestState;
import abuldovi.telegram.telegramApp.util.TransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.regex.PatternSyntaxException;

@Component
public class MenuHandler {

    private final TransactionState transactionState;
    private final BotStateMenu botStateMenu;
    private final Keyboards keyboards;
    private final RequestState requestState;
    private final EditTransactionHandler editTransactionHandler;

    private final AddTransactionHandler addTransactionHandler;
    private final ShowTransactionsHandler showTransactionsHandler;
    private final DeleteTransactionHandler deleteTransactionHandler;

    @Autowired
    public MenuHandler(TransactionState transactionState, BotStateMenu botStateMenu, Keyboards keyboards, RequestState requestState, EditTransactionHandler editTransactionHandler, AddTransactionHandler addTransactionHandler, ShowTransactionsHandler showTransactionsHandler, DeleteTransactionHandler deleteTransactionHandler) {
        this.transactionState = transactionState;
        this.botStateMenu = botStateMenu;
        this.keyboards = keyboards;
        this.requestState = requestState;
        this.editTransactionHandler = editTransactionHandler;
        this.addTransactionHandler = addTransactionHandler;
        this.showTransactionsHandler = showTransactionsHandler;
        this.deleteTransactionHandler = deleteTransactionHandler;
    }


    public EditMessageText handleCallback(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
       int messageId = callbackQuery.getMessage().getMessageId();

        String data = callbackQuery.getData();

        if (data.equals("cancel")) {
            transactionState.removeTransactionState(chatId);
            return addTransactionHandler.startEdit(chatId,  messageId);
        }

        if (botStateMenu.containsChatId(chatId)) {
            if (botStateMenu.getBotState(chatId).equals(BotState.ADDCATEGORY)) {
                Transaction transaction = transactionState.getTransactionState(chatId);
                transaction.setCategory(callbackQuery.getData());
                transactionState.changeTransactionState(chatId, transaction);
                return addTransactionHandler.showSource(chatId,  messageId);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.ADDSOURCE)) {
                Transaction transaction = transactionState.getTransactionState(chatId);
                transaction.setSource(callbackQuery.getData());
                transactionState.changeTransactionState(chatId, transaction);
                return addTransactionHandler.addSource(chatId,  messageId);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SELECTDIFMONTH)
                    || botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSELECTDIFMONTH)
                    || botStateMenu.getBotState(chatId).equals(BotState.SOURCESELECTDIFMONTH)) {
                requestState.changeRequestYear(chatId, data);
                return showTransactionsHandler.showMonth(chatId,  messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SELECTMONTH)
                    || botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSELECTMONTH)
                    || botStateMenu.getBotState(chatId).equals(BotState.SOURCESELECTMONTH)) {
                return showTransactionsHandler.showMonthResult(chatId,  messageId, data, requestState.getRequest(chatId).getYear());
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SHOWYEARS)
                    || botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSHOWYEARS)
                    || botStateMenu.getBotState(chatId).equals(BotState.SOURCESHOWYEARS)) {
                return showTransactionsHandler.selectYear(chatId,  messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEXPENSES)) {
                return showTransactionsHandler.selectCategoryExpensesPeriod(chatId,  messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSES)) {
                return showTransactionsHandler.selectSourceExpensesPeriod(chatId,  messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSES)) {
                return showTransactionsHandler.selectSourceExpensesPeriod(chatId,  messageId, data);
            }
            if(botStateMenu.getBotState(chatId).equals(BotState.EDITTRANSACTION)) {
                switch (data) {
                    case "editValue":
                        return editTransactionHandler.editValue(chatId,  messageId);
                    case "editCategory":
                        return editTransactionHandler.editCategory(chatId,  messageId);
                    case "editSource":
                        return editTransactionHandler.editSource(chatId,  messageId);
                    case "editDate":
                        return editTransactionHandler.editDate(chatId,  messageId);

                }
            }
            if(botStateMenu.getBotState(chatId).equals(BotState.DELETETRANSACTION)){
                switch (data){
                case "yesDelete":
                    return deleteTransactionHandler.deleteMessageFinalYes(chatId,  messageId);
                case "noDelete":
                    return deleteTransactionHandler.deleteMessageFinalNo(chatId, messageId);

                }
            }
            if(botStateMenu.getBotState(chatId).equals(BotState.DELETETRANSACTIONSTEP2)){
                switch (data){
                    case "yesDelete":
                        return deleteTransactionHandler.deleteTransaction(chatId, messageId);
                    case "noDelete":
                        return deleteTransactionHandler.deleteMessageFinalNo(chatId, messageId);

                }
            }
            if(botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEDITTRANSACTION)){
                return editTransactionHandler.addCategory(chatId,  messageId, data);
            }
            if(botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEDITTRANSACTION)){
                return editTransactionHandler.addSource(chatId,  messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SHOWEXPENSES)
                    || botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEXPENSESPERIOD)
                    || botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSESPERIOD)) {
                switch (data) {
                    case "yearly":
                        return showTransactionsHandler.showYearsExpenses(chatId,  messageId);
                    case "thisMonth":
                        return showTransactionsHandler.showThisMonth(chatId,  messageId);
                    case "difMonth":
                        return showTransactionsHandler.showDifferentMonthYears(chatId,  messageId);
                    case "category":
                        return showTransactionsHandler.showCategoryExpense(chatId,  messageId);
                    case "source":
                        return showTransactionsHandler.showSourceExpense(chatId,  messageId);
                    case "test":
                        return showTransactionsHandler.test(chatId,  messageId);
                }

                return addTransactionHandler.addSource(chatId,  messageId);
            }
        }

        switch (data) {
            case "showExpense":
                return showTransactionsHandler.showExpenses(chatId,  messageId);
            case "addExpense":
                return addTransactionHandler.addExpenses(chatId,  messageId);
            case "editExpense":
                return editTransactionHandler.editExpense(chatId,  messageId);
            case "deleteExpense":
                return deleteTransactionHandler.deleteStart(chatId,  messageId);
            case "test":
                return showTransactionsHandler.test(chatId,  messageId);
            default:
                return EditMessageText.builder().chatId(chatId).messageId( messageId).text("Wrong command, try again").build();
        }
    }

    public SendMessage handleMessage(Message message) {
        long chatId = message.getChatId();
        SendMessage sendMessageText = SendMessage.builder()
                .chatId(chatId)
                .text("Wrong command")
                .build();


            try {
                if (botStateMenu.getBotState(chatId).equals(BotState.ADDVALUE))
                {
                    sendMessageText = addTransactionHandler.addValue(chatId, message);
                }
                if (botStateMenu.getBotState(chatId).equals(BotState.EDITEXPENSEADDVALUE))
                {
                    sendMessageText = editTransactionHandler.findTransaction(message);
                }
                if (botStateMenu.getBotState(chatId).equals(BotState.ADDVALUEEDITTRANSACTION))
                {
                    sendMessageText = editTransactionHandler.addValue(message);
                }
                if (botStateMenu.getBotState(chatId).equals(BotState.EDITDATEEDITTRANSACTION))
                {
                    sendMessageText = editTransactionHandler.addDate(message);
                }
                if (botStateMenu.getBotState(chatId).equals(BotState.DELETETRANSACTION))
                {
                    sendMessageText = deleteTransactionHandler.findTransaction(message);
                }
            } catch (NumberFormatException | TransactionNullValueException | PatternSyntaxException |
                     DateTimeParseException e) {
                sendMessageText.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getHomeKeyboard()).build());
                if (e.getClass().equals(NumberFormatException.class)) {
                    sendMessageText.setText("Type a number!");
                } else if (e.getClass().equals(TransactionNullValueException.class)) {
                    sendMessageText.setText(e.getMessage());
                } else if (e.getClass().equals(PatternSyntaxException.class)) {
                    sendMessageText.setText(e.getMessage());
                } else if (e.getClass().equals(DateTimeParseException.class)) {
                    sendMessageText.setText("Wrong day, month or year");
                }



            }




        return sendMessageText;
    }

    public SendMessage menu(long chatId) {

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .entities(Collections.singletonList(MessageEntity.builder().offset(0).type("bold").length(9).build()))
                .text("Main menu")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getStartKeyboard()).build())
                .build();


        botStateMenu.changeBotState(chatId, BotState.START);

        return message;

    }

    public SendMessage start(long chatId, String name) {

        String text = "Hello, " + name + "!" +
                "\n\nThis is your personal expense tracker." +
                "\nJust open the menu in the bottom left corner or type /set_finance and take full control of your expenses!" +
                "\n\nGood luck!";


        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .build();

        botStateMenu.changeBotState(chatId, BotState.START);

        return message;

    }

}



