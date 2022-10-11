package abuldovi.telegram.telegramApp.handlers;

import abuldovi.telegram.telegramApp.enums.BotState;
import abuldovi.telegram.telegramApp.models.Transaction;
import abuldovi.telegram.telegramApp.service.TransactionService;
import abuldovi.telegram.telegramApp.util.BotStateMenu;
import abuldovi.telegram.telegramApp.util.TransactionState;
import abuldovi.telegram.telegramApp.util.YearState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class MenuHandler {

    private final TransactionService transactionService;
    private final TransactionState transactionState;
    private final BotStateMenu botStateMenu;
    private final Keyboards keyboards;
    private final YearState yearState;

    @Autowired
    public MenuHandler(TransactionService transactionService, TransactionState transactionState, BotStateMenu botStateMenu, Keyboards keyboards, YearState yearState) {
        this.transactionService = transactionService;
        this.transactionState = transactionState;
        this.botStateMenu = botStateMenu;
        this.keyboards = keyboards;
        this.yearState = yearState;
    }


    public EditMessageText handleCallback(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        long messageId = callbackQuery.getMessage().getMessageId();

        String data = callbackQuery.getData();

        if (data.equals("cancel")){
            transactionState.removeTransactionState(chatId);
            return startEdit(chatId, (int)messageId);
        }

        if(botStateMenu.containsChatId(chatId)) {
            if (botStateMenu.getBotState(chatId).equals(BotState.ADDCATEGORY)) {
                Transaction transaction = transactionState.getTransactionState(chatId);
                transaction.setCategory(callbackQuery.getData());
                transactionState.changeTransactionState(chatId, transaction);
                return showSource(chatId, (int) messageId);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.ADDSOURCE)) {
                Transaction transaction = transactionState.getTransactionState(chatId);
                transaction.setSource(callbackQuery.getData());
                transactionState.changeTransactionState(chatId, transaction);
                return addSource(chatId, (int) messageId);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SELECTDIFMONTH)) {
                yearState.changeYearState(chatId, data);
                return showMonth(chatId, (int) messageId, data);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SELECTMONTH)) {
                String year = yearState.getYearState(chatId);
                return showMonthResult(chatId, (int) messageId, data, year);
            }
            if (botStateMenu.getBotState(chatId).equals(BotState.SHOWYEARS)) {
                return selectYear(chatId, (int) messageId, data);
            }

            if (botStateMenu.getBotState(chatId).equals(BotState.SHOWEXPENSES)) {
                switch (data) {
                    case "yearly":
                        return showYears(chatId, (int) messageId);
                    case "thisMonth":
                        return showThisMonth(chatId, (int) messageId);
                    case "difMonth":
                        return showDifferentMonthYears(chatId, (int) messageId);
                    case "test":
                        return test(chatId, (int) messageId);
                }

                return addSource(chatId, (int) messageId);
            }
        }

        switch (data) {
            case "start": return startEdit(chatId, (int)messageId);
            case "showExpense": return showExpenses(chatId, (int)messageId);
            case "addExpense": return addExpenses(chatId, (int)messageId);
            case "test": return test(chatId, (int)messageId);
            default: return EditMessageText.builder().chatId(chatId).messageId((int)messageId).text("Smth wrong").build();
        }
    }

    private EditMessageText showMonthResult(long chatId, int messageId, String month, String year) {

        String message;

        botStateMenu.changeBotState(chatId, BotState.MONTHRESULT);

        Integer sum = transactionService.getSumByMonth(chatId, month, year);

        List<Transaction> transactions = transactionService.getByMonthAndYear(chatId, month, year);

        if(transactions == null) {
            message = "There is no transaction in this month";
        } else message = transactionStringBuilder(transactions, sum);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getCancelKeyboard()).build())
                .text(message)
                .build();
    }


    public SendMessage handleMessage(Message message){
        long chatId = message.getChatId();
        SendMessage sendMessageText = SendMessage.builder()
                .chatId(chatId)
                .text("HandleMessage")
                .build();

        switch (botStateMenu.getBotState(chatId)) {
            case ADDVALUE:
                Transaction transaction = new Transaction();
                transaction.setValue(Integer.parseInt(message.getText()));
                transactionState.changeTransactionState(chatId, transaction);
                sendMessageText = showCategory(chatId);
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

    public EditMessageText showExpenses(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.SHOWEXPENSES);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text("Choose the option")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getExpensesKeyboard()).build())
                .build();

    }
    public EditMessageText addExpenses(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.ADDVALUE);

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getCancelKeyboard()).build())
                .text("Please enter the number")
                .build();

    }



    public SendMessage showCategory(long chatId) {

        botStateMenu.changeBotState(chatId, BotState.ADDCATEGORY);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getCategoryKeyboard()).build())
                .text("Choose the category")
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


    private EditMessageText showMonth(long chatId, int messageId, String data) {

        botStateMenu.changeBotState(chatId, BotState.SELECTMONTH);

        List<Integer> months = transactionService.findMonthsByYear(chatId, data);

        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getMonthsKeyboard(months)).build())
                .text("Choose month")
                .build();
    }




    private EditMessageText showDifferentMonthYears(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.SELECTDIFMONTH);

        List<Integer> years = transactionService.findYears(chatId);

        return EditMessageText.builder()
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(keyboards.getYearsKeyboard(years))
                        .build())
                .chatId(chatId)
                .text("Choose year")
                .build();
    }

    private EditMessageText showThisMonth(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.THISMONTH);

        Calendar cal = Calendar.getInstance();

        return showMonthResult(chatId, messageId, String.valueOf(cal.get(Calendar.MONTH)+1), String.valueOf(cal.get(Calendar.YEAR)));
    }

    private EditMessageText showYears(long chatId, int messageId) {

        EditMessageText editMessageText;

        botStateMenu.changeBotState(chatId, BotState.SHOWYEARS);

        List<Integer> years = transactionService.findYears(chatId);

        if(years.isEmpty()) {
            editMessageText = errorMessage(chatId, messageId, "No transactions yet");
        } else editMessageText = EditMessageText.builder()
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getYearsKeyboard(years)).build())
                .chatId(chatId)
                .text("Years")
                .build();



        return editMessageText;

    }

    private EditMessageText selectYear(long chatId, int messageId, String data) {

        botStateMenu.changeBotState(chatId, BotState.SELECTYEAR);

        List<Transaction> years = transactionService.findByYear(chatId, data);

        String message = transactionStringBuilder(years, transactionService.getSumByYear(chatId, data));

        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getCancelKeyboard()).build())
                .text(message)
                .build();
    }


    public EditMessageText test(long chatId, int messageId){

        String message = transactionStringBuilder(transactionService.findAll(chatId), 0);

        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text(message)
                .build();
    }

    private String transactionStringBuilder(List<Transaction> transactions, int sum){

        StringBuilder stringBuilder = new StringBuilder();
        for (Transaction transaction : transactions) {
            stringBuilder
                    .append("Transaction: ").append(transaction.getTransaction_id())
                    .append("\nCategory: ").append(transaction.getCategory())
                    .append("\nTime: ").append(transaction.getTimestamp())
                    .append("\nSource: ").append(transaction.getSource())
                    .append("\nValue: ").append(transaction.getValue())
                    .append("\n-----------------\n");

        }
        stringBuilder.append("\nSum: ").append(sum);

        return stringBuilder.toString();
    }

    private EditMessageText errorMessage(long chatId, int messageId, String error){
        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboards.getCancelKeyboard()).build())
                .text(error)
                .build();
    }







}


