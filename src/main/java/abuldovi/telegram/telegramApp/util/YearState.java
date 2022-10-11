package abuldovi.telegram.telegramApp.util;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class YearState {
    private final Map<Long, String> yearMap = new LinkedHashMap<>();

    public void changeYearState(long chatId, String year){
        if(!yearMap.containsKey(chatId)){
            yearMap.put(chatId, year);
        } else
            yearMap.replace(chatId, year);
    }

    public boolean containsChatId(long chatId) { return yearMap.containsKey(chatId); }

    public String getYearState(long chatId){
        return yearMap.get(chatId);
    }
}
