package abuldovi.telegram.telegramApp.handlers;

import abuldovi.telegram.telegramApp.enums.BotState;
import abuldovi.telegram.telegramApp.models.Transaction;
import abuldovi.telegram.telegramApp.service.TransactionService;
import abuldovi.telegram.telegramApp.util.BotStateMenu;
import abuldovi.telegram.telegramApp.util.RequestState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

@Component
public class ShowTransactionsHandler {

    private final TransactionService transactionService;
    private final BotStateMenu botStateMenu;
    private final Keyboards keyboards;
    private final RequestState requestState;


    public ShowTransactionsHandler(TransactionService transactionService, BotStateMenu botStateMenu, Keyboards keyboards, RequestState requestState) {
        this.transactionService = transactionService;
        this.botStateMenu = botStateMenu;
        this.keyboards = keyboards;
        this.requestState = requestState;
    }

    public EditMessageText showExpenses(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.SHOWEXPENSES);

        return editMessageTextBuilder(chatId, messageId, "Choose the option", keyboards.getExpensesKeyboard(false));
    }

    public EditMessageText showCategoryExpense(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.CHOOSECATEGORYEXPENSES);

        return editMessageTextBuilder(chatId, messageId, "Choose the category", keyboards.getCategoryKeyboard());
    }

    public EditMessageText showSourceExpense(long chatId, int messageId) {

        botStateMenu.changeBotState(chatId, BotState.CHOOSESOURCEEXPENSES);

        return editMessageTextBuilder(chatId, messageId, "Choose the source", keyboards.getSourceKeyboard());


    }

    public EditMessageText selectCategoryExpensesPeriod(long chatId, int messageId, String category) {

        requestState.changeRequestCategory(chatId, category);

        botStateMenu.changeBotState(chatId, BotState.CHOOSECATEGORYEXPENSESPERIOD);

        return editMessageTextBuilder(chatId, messageId, "Choose the option", keyboards.getExpensesKeyboard(true));

    }

    public EditMessageText selectSourceExpensesPeriod(long chatId, int messageId, String source) {

        requestState.changeRequestSource(chatId, source);

        botStateMenu.changeBotState(chatId, BotState.CHOOSESOURCEEXPENSESPERIOD);

        return editMessageTextBuilder(chatId, messageId, "Choose the option", keyboards.getExpensesKeyboard(true));

    }

    public EditMessageText showMonth(long chatId, int messageId, String data) {

        if (botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSELECTDIFMONTH)) {
            botStateMenu.changeBotState(chatId, BotState.CATEGORYSELECTMONTH);
        } else if (botStateMenu.getBotState(chatId).equals(BotState.SOURCESELECTDIFMONTH)) {
            botStateMenu.changeBotState(chatId, BotState.SOURCESELECTMONTH);
        } else {
            botStateMenu.changeBotState(chatId, BotState.SELECTMONTH);
        }

        List<Integer> months = transactionService.findMonthsByYear(chatId, data);

        return editMessageTextBuilder(chatId, messageId, "Choose month", keyboards.getMonthsKeyboard(months));
    }


    public EditMessageText showDifferentMonthYears(long chatId, int messageId) {


        if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEXPENSESPERIOD)) {
            botStateMenu.changeBotState(chatId, BotState.CATEGORYSELECTDIFMONTH);
        } else if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSESPERIOD)) {
            botStateMenu.changeBotState(chatId, BotState.SOURCESELECTDIFMONTH);
        } else {
            botStateMenu.changeBotState(chatId, BotState.SELECTDIFMONTH);
        }

        List<Integer> years = transactionService.findYears(chatId);

        return editMessageTextBuilder(chatId, messageId, "Choose year", keyboards.getYearsKeyboard(years));

    }

    public EditMessageText showThisMonth(long chatId, int messageId) {

        Calendar cal = Calendar.getInstance();

        if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEXPENSESPERIOD)) {
            botStateMenu.changeBotState(chatId, BotState.CATEGORYTHISMONTH);
            return showCategoryMonthResult(chatId, messageId, String.valueOf(cal.get(Calendar.MONTH) + 1), String.valueOf(cal.get(Calendar.YEAR)));

        } else if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSESPERIOD)) {
            botStateMenu.changeBotState(chatId, BotState.SOURCETHISMONTH);
            return showSourceMonthResult(chatId, messageId, String.valueOf(cal.get(Calendar.MONTH) + 1), String.valueOf(cal.get(Calendar.YEAR)));
        } else {
            botStateMenu.changeBotState(chatId, BotState.THISMONTH);
            return showMonthResult(chatId, messageId, String.valueOf(cal.get(Calendar.MONTH) + 1), String.valueOf(cal.get(Calendar.YEAR)));

        }


    }

    public EditMessageText showYearsExpenses(long chatId, int messageId) {

        EditMessageText editMessageText;

        if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSECATEGORYEXPENSESPERIOD)) {
            botStateMenu.changeBotState(chatId, BotState.CATEGORYSHOWYEARS);
        } else if (botStateMenu.getBotState(chatId).equals(BotState.CHOOSESOURCEEXPENSESPERIOD)) {
            botStateMenu.changeBotState(chatId, BotState.SOURCESHOWYEARS);
        } else botStateMenu.changeBotState(chatId, BotState.SHOWYEARS);

        List<Integer> years = transactionService.findYears(chatId);

        if (years.isEmpty()) {
            editMessageText = errorMessage(chatId, messageId, "No transactions yet");
        } else editMessageText = editMessageTextBuilder(chatId, messageId, "Years", keyboards.getYearsKeyboard(years));

        return editMessageText;

    }

    public EditMessageText selectYear(long chatId, int messageId, String data) {

        String message;
        List<Transaction> years;

        if (botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSHOWYEARS)) {
            botStateMenu.changeBotState(chatId, BotState.CATEGORYSELECTYEAR);
            years = transactionService.findByYearAndCategory(chatId, data, requestState.getRequest(chatId).getCategory());
            if (years.isEmpty()) {
                return errorMessage(chatId, messageId, "There is no transactions in this year");
            }
            message = transactionStringBuilder(years, transactionService.getSumByYearAndByCategory(chatId, data, requestState.getRequest(chatId).getCategory()));
        } else if (botStateMenu.getBotState(chatId).equals(BotState.SOURCESHOWYEARS)) {
            botStateMenu.changeBotState(chatId, BotState.SOURCESELECTYEAR);
            years = transactionService.findByYearAndSource(chatId, data, requestState.getRequest(chatId).getSource());
            if (years.isEmpty()) {
                return errorMessage(chatId, messageId, "There is no transactions in this year");
            }
            message = transactionStringBuilder(years, transactionService.getSumByYearAndBySource(chatId, data, requestState.getRequest(chatId).getSource()));
        } else {
            botStateMenu.changeBotState(chatId, BotState.SELECTYEAR);
            years = transactionService.findByYear(chatId, data);
            if (years.isEmpty()) {
                return errorMessage(chatId, messageId, "There is no transactions in this year");
            }
            message = transactionStringBuilder(years, transactionService.getSumByYear(chatId, data));
        }

        return editMessageTextBuilder(chatId, messageId, message, keyboards.getHomeKeyboard());
    }


    public EditMessageText test(long chatId, int messageId) {

        String message = transactionStringBuilder(transactionService.findAll(chatId), 0);

        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text(message)
                .build();
    }

    public EditMessageText errorMessage(long chatId, int messageId, String error) {
        return editMessageTextBuilder(chatId, messageId, error, keyboards.getHomeKeyboard());
    }

    public EditMessageText showMonthResult(long chatId, int messageId, String month, String year) {

        String message;
        Integer sum;
        List<Transaction> transactions;

        if (botStateMenu.getBotState(chatId).equals(BotState.CATEGORYSELECTMONTH)) {
            String category = requestState.getRequest(chatId).getCategory();
            sum = transactionService.getSumByMonthAndByYearAndByCategory(chatId, month, year, category);
            transactions = transactionService.getByMonthAndYearAndCategory(chatId, month, year, category);
        } else if (botStateMenu.getBotState(chatId).equals(BotState.SOURCESELECTMONTH)) {
            String source = requestState.getRequest(chatId).getSource();
            sum = transactionService.getSumByMonthAndByYearAndSource(chatId, month, year, source);
            transactions = transactionService.getByMonthAndYearAndSource(chatId, month, year, source);
        } else {

            sum = transactionService.getSumByMonth(chatId, month, year);
            transactions = transactionService.getByMonthAndYear(chatId, month, year);
        }
        botStateMenu.changeBotState(chatId, BotState.MONTHRESULT);


        if (transactions.isEmpty()) {
            message = "There is no transaction in this month";
        } else message = transactionStringBuilder(transactions, sum);

        return editMessageTextBuilder(chatId, messageId, message, keyboards.getHomeKeyboard());
    }

    public EditMessageText showCategoryMonthResult(long chatId, int messageId, String month, String year) {

        String message;
        String category = requestState.getRequest(chatId).getCategory();

        botStateMenu.changeBotState(chatId, BotState.MONTHRESULT);

        Integer sum = transactionService.getSumByMonthAndByYearAndByCategory(chatId, month, year, category);
        List<Transaction> transactions = transactionService.getByMonthAndYearAndCategory(chatId, month, year, category);

        if (transactions.isEmpty()) {
            message = "There is no transaction in this month";
        } else message = transactionStringBuilder(transactions, sum);

        return editMessageTextBuilder(chatId, messageId, message, keyboards.getHomeKeyboard());
    }

    public EditMessageText showSourceMonthResult(long chatId, int messageId, String month, String year) {

        String message;
        String source = requestState.getRequest(chatId).getSource();

        botStateMenu.changeBotState(chatId, BotState.MONTHRESULT);

        Integer sum = transactionService.getSumByMonthAndByYearAndSource(chatId, month, year, source);
        List<Transaction> transactions = transactionService.getByMonthAndByYearAndSource(chatId, month, year, source);

        if (transactions.isEmpty()) {
            message = "There is no transaction in this month";
        } else message = transactionStringBuilder(transactions, sum);

        return editMessageTextBuilder(chatId, messageId, message, keyboards.getHomeKeyboard());
    }

    public EditMessageText editMessageTextBuilder(long chatId, int messageId, String text, List<List<InlineKeyboardButton>> keyboard) {

        return EditMessageText.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .text(text)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(keyboard).build())
                .build();

    }

    public String transactionStringBuilder(List<Transaction> transactions, int sum) {

        StringBuilder stringBuilder = new StringBuilder();
        for (Transaction transaction : transactions) {
            stringBuilder
                    .append("Transaction: ").append(transaction.getTransaction_id())
                    .append("\nCategory: ").append(transaction.getCategory())
                    .append("\nDate: ").append(transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .append("\nSource: ").append(transaction.getSource())
                    .append("\nValue: ").append(transaction.getValue())
                    .append("\n------------------------\n");

        }
        stringBuilder.append("\nSum: ").append(sum);

        return stringBuilder.toString();
    }

    public String transactionStringBuilder(Transaction transaction) {
        return "Transaction: " + transaction.getTransaction_id() +
                "\nCategory: " + transaction.getCategory() +
                "\nDate: " + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                "\nSource: " + transaction.getSource() +
                "\nValue: " + transaction.getValue() +
                "\n------------------------\n";
    }


}
