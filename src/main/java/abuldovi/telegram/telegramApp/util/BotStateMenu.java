package abuldovi.telegram.telegramApp.util;

import abuldovi.telegram.telegramApp.enums.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotStateMenu {
    private final Map<Long, BotState> botStateMap = new HashMap<>();

    public void changeBotState(long chatId, BotState botState){
        if(!botStateMap.containsKey(chatId)){
            botStateMap.put(chatId, BotState.START);
        } else
            botStateMap.replace(chatId, botState);
    }

    public boolean containsChatId(long chatId) { return botStateMap.containsKey(chatId); }

    public BotState getBotState(long chatId){
        return botStateMap.get(chatId);
    }
}
