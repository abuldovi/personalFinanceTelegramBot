package abuldovi.telegram.telegramApp.handlers;

import abuldovi.telegram.telegramApp.enums.Categories;
import abuldovi.telegram.telegramApp.enums.Emoji;
import abuldovi.telegram.telegramApp.enums.Source;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Component
public class Keyboards {

    List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();

    public List<List<InlineKeyboardButton>> getStartKeyboard() {

        buttonList.clear();

        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.MONEY_WITH_WINGS + " Show expenses").callbackData("showExpense").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.HEAVY_PLUS_SIGN + " Add expense").callbackData("addExpense").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text("test").callbackData("test").build()));
        return buttonList;
    }

    public List<List<InlineKeyboardButton>> getExpensesKeyboard() {

        buttonList.clear();

        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text("Yearly").callbackData("yearly").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text("Different month").callbackData("difMonth").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text("This month").callbackData("thisMonth").build()));

        addCancel();

        return buttonList;

    }

    public List<List<InlineKeyboardButton>> getCategoryKeyboard() {

        buttonList.clear();

        for(Categories category: Categories.values()){
            buttonList.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .text(category.label)
                    .callbackData(category.name())
                    .build()));
        }

        addCancel();

        return buttonList;

    }

    public List<List<InlineKeyboardButton>> getSourceKeyboard() {

        buttonList.clear();

        for(Source source: Source.values()){
            buttonList.add(Arrays.asList(InlineKeyboardButton.builder()
                    .text(source.label)
                    .callbackData(source.name())
                    .build()));
        }

        addCancel();

        return buttonList;
    }

    public  List<List<InlineKeyboardButton>> getYearsKeyboard(List<Integer> years)  {

        buttonList.clear();

        for(Integer year: years){
            buttonList.add(Arrays.asList(InlineKeyboardButton.builder()
                    .text(year.toString())
                    .callbackData(year.toString())
                    .build()));
        }

        addCancel();

        return buttonList;
    }

    public  List<List<InlineKeyboardButton>> getMonthsKeyboard(List<Integer> months)  {

        buttonList.clear();



        for(Integer month: months){
            buttonList.add(Arrays.asList(InlineKeyboardButton.builder()
                    .text(Month.of(month).getDisplayName( TextStyle.FULL, Locale.ENGLISH))
                    .callbackData(month.toString())
                    .build()));
        }

        addCancel();

        return buttonList;
    }

    public List<List<InlineKeyboardButton>> getCancelKeyboard() {

        buttonList.clear();

        addCancel();

        return buttonList;
    }


    public void addCancel(){
        buttonList.add(Collections.singletonList(
                InlineKeyboardButton.builder().text( Emoji.CROSS_MARK + " Cancel").callbackData("cancel").build()
        ));
    }
}
