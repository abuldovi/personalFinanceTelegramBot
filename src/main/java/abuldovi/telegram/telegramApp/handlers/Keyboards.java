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
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.PENCIL + " Edit expense").callbackData("editExpense").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.CROSS_MARK + " Delete expense").callbackData("deleteExpense").build()));

        return buttonList;
    }

    public List<List<InlineKeyboardButton>> getExpensesKeyboard(boolean shortVersion) {

        buttonList.clear();

        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text("️Yearly").callbackData("yearly").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text("Different month").callbackData("difMonth").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text("This month").callbackData("thisMonth").build()));

        if(!shortVersion){
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.BAR + " By category").callbackData("category").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.CREDIT_CARD + " By source").callbackData("source").build()));
        }

        addHome();

        return buttonList;

    }

    public List<List<InlineKeyboardButton>> getEditKeyboard() {

        buttonList.clear();

        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.MONEY_BUG + " Change value").callbackData("editValue").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.CALENDAR + " Change date").callbackData("editDate").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.BAR + " Change category").callbackData("editCategory").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.CREDIT_CARD + " Change source").callbackData("editSource").build()));

        addHome();

        return buttonList;
    }

    public List<List<InlineKeyboardButton>> getDeleteKeyboard() {

        buttonList.clear();

        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(Emoji.CROSS_MARK + " Yes").callbackData("yesDelete").build()));
        buttonList.add(Collections.singletonList(InlineKeyboardButton.builder().text(" No").callbackData("noDelete").build()));


        addHome();

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

        addHome();

        return buttonList;

    }

    public List<List<InlineKeyboardButton>> getSourceKeyboard() {

        buttonList.clear();

        for(Source source: Source.values()){
            buttonList.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .text(source.label)
                    .callbackData(source.name())
                    .build()));
        }

        addHome();

        return buttonList;
    }

    public  List<List<InlineKeyboardButton>> getYearsKeyboard(List<Integer> years)  {

        buttonList.clear();

        for(Integer year: years){
            buttonList.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .text(year.toString())
                    .callbackData(year.toString())
                    .build()));
        }

        addHome();

        return buttonList;
    }

    public  List<List<InlineKeyboardButton>> getMonthsKeyboard(List<Integer> months)  {

        buttonList.clear();



        for(Integer month: months){
            buttonList.add(Collections.singletonList(InlineKeyboardButton.builder()
                    .text(Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                    .callbackData(month.toString())
                    .build()));
        }

        addHome();

        return buttonList;
    }

    public List<List<InlineKeyboardButton>> getHomeKeyboard() {

        buttonList.clear();

        addHome();

        return buttonList;
    }


    public void addHome(){
        buttonList.add(Collections.singletonList(
                InlineKeyboardButton.builder().text( Emoji.HOUSE + " Home").callbackData("cancel").build()
        ));
    }
}
