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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MenuHandler {

    private final TransactionState transactionState;
    private final BotStateMenu botStateMenu;
    private final Keyboards keyboards;
    private final RequestState requestState;
    private final EditTransactionHandler editTransactionHandler;

    private final AddTransactionHandler addTransactionHandler;
    private final ShowTransactionsHandler showTransactionsHandler;

    @Autowired
    public MenuHandler(TransactionState transactionState, BotStateMenu botStateMenu, Keyboards keyboards, RequestState requestState, EditTransactionHandler editTransactionHandler, AddTransactionHandler addTransactionHandler, ShowTransactionsHandler showTransactionsHandler) {
        this.transactionState = transactionState;
        this.botStateMenu = botStateMenu;
        this.keyboards = keyboards;
        this.requestState = requestState;
        this.editTransactionHandler = editTransactionHandler;
        this.addTransactionHandler = addTransactionHandler;
        this.showTransactionsHandler = showTransactionsHandler;
    }


    public EditMessageText handleCallback(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        long messageId = callbackQuery.getMessage().getMessageId();

        String data = callbackQuery.getData();

        if (data.equals("cancel")) {
            transactionState.removeTransactionState(chatId);
            return addTransactionHandler.startEdit(chatId, (int) messageId);
        }

        if (botStateMenu.containsChatId(chatId)) {
            if (botStateMenu.getBotState(chatId).equals(BotState.ADDCATEGORY)) {
                Transaction transaction = transactionState.getTransactionState(chatId);
                transaction.setCategory(callbackQuery.getData());
                transactionState.changeTransactionState(chatId, transaction);
                return addTransactionHandler.showSource(chatId, (int) messageId);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.ADDSOURCE)) {
                Transaction transaction = transactionState.getTransactionState(chatId);
                transaction.setSource(callbackQuery.getData());
                transactionState.changeTransactionState(chatId, transaction);
                return addTransactionHandler.addSource(chatId, (int) messageId);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SELECTDIFMONTH)
                    || botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSELECTDIFMONTH)
                    || botStateMenu.getBotState(chatId).equals(BotState.SOURCESELECTDIFMONTH)) {
                requestState.changeRequestYear(chatId, data);
                return showTransactionsHandler.showMonth(chatId, (int) messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SELECTMONTH)
                    || botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSELECTMONTH)
                    || botStateMenu.getBotState(chatId).equals(BotState.SOURCESELECTMONTH)) {
                return showTransactionsHandler.showMonthResult(chatId, (int) messageId, data, requestState.getRequest(chatId).getYear());
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SHOWYEARS)
                    || botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSHOWYEARS)
                    || botStateMenu.getBotState(chatId).equals(BotState.SOURCESHOWYEARS)) {
                return showTransactionsHandler.selectYear(chatId, (int) messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEXPENSES)) {
                return showTransactionsHandler.selectCategoryExpensesPeriod(chatId, (int) messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSES)) {
                return showTransactionsHandler.selectSourceExpensesPeriod(chatId, (int) messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSES)) {
                return showTransactionsHandler.selectSourceExpensesPeriod(chatId, (int) messageId, data);
            }
            if(botStateMenu.getBotState(chatId).equals(BotState.EDITTRANSACTION)){
                switch (data){
                case "editValue":
                    return editTransactionHandler.editValue(chatId, (int) messageId);
                case "editCategory":
                    return editTransactionHandler.editCategory(chatId, (int) messageId);
                case "editSource":
                    return editTransactionHandler.editSource(chatId, (int) messageId);
                case "editDate":
                    return null;

                }
            }
            if(botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEDITTRANSACTION)){
                return editTransactionHandler.addCategory(chatId, (int) messageId, data);
            }
            if(botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEDITTRANSACTION)){
                return editTransactionHandler.addSource(chatId, (int) messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SHOWEXPENSES)
                    || botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEXPENSESPERIOD)
                    || botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSESPERIOD)) {
                switch (data) {
                    case "yearly":
                        return showTransactionsHandler.showYearsExpenses(chatId, (int) messageId);
                    case "thisMonth":
                        return showTransactionsHandler.showThisMonth(chatId, (int) messageId);
                    case "difMonth":
                        return showTransactionsHandler.showDifferentMonthYears(chatId, (int) messageId);
                    case "category":
                        return showTransactionsHandler.showCategoryExpense(chatId, (int) messageId);
                    case "source":
                        return showTransactionsHandler.showSourceExpense(chatId, (int) messageId);
                    case "test":
                        return showTransactionsHandler.test(chatId, (int) messageId);
                }

                return addTransactionHandler.addSource(chatId, (int) messageId);
            }
        }

        switch (data) {
            case "start":
                return addTransactionHandler.startEdit(chatId, (int) messageId);
            case "showExpense":
                return showTransactionsHandler.showExpenses(chatId, (int) messageId);
            case "addExpense":
                return addTransactionHandler.addExpenses(chatId, (int) messageId);
            case "editExpense":
                return editTransactionHandler.editExpense(chatId, (int) messageId);
            case "test":
                return showTransactionsHandler.test(chatId, (int) messageId);
            default:
                return EditMessageText.builder().chatId(chatId).messageId((int) messageId).text("Smth wrong").build();
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
            } catch (NumberFormatException | TransactionNullValueException e) {
                sendMessageText.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getHomeKeyboard()).build());
                if (e.getClass().equals(NumberFormatException.class)) {
                    sendMessageText.setText("Type a number!");
                } else if (e.getClass().equals(TransactionNullValueException.class)) {
                    sendMessageText.setText(e.getMessage());
                }

            }




        return sendMessageText;
    }

    public SendMessage start(long chatId) {

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("Choose the option")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getStartKeyboard()).build())
                .build();

        botStateMenu.changeBotState(chatId, BotState.START);

        System.out.println(botStateMenu.getBotState(chatId));
        return message;

    }

}



