package abuldovi.telegram.telegramApp.util;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RequestState {
    private final Map<Long, Request> requestMap = new LinkedHashMap<>();


    public void changeRequest(long chatId, Request request){
        if(!requestMap.containsKey(chatId)){
            requestMap.put(chatId, request);
        } else
            requestMap.replace(chatId, request);
    }

    public void changeRequestYear(long chatId, String year){

        if(!requestMap.containsKey(chatId)){
            Request request = new Request();
            request.setYear(year);
            requestMap.put(chatId, request);
        } else {
            Request request = requestMap.get(chatId);
            request.setYear(year);
            requestMap.replace(chatId, request);
        }
    }
    public void changeRequestCategory(long chatId, String category){

        if(!requestMap.containsKey(chatId)){
            Request request = new Request();
            request.setCategory(category);
            requestMap.put(chatId, request);
        } else {
            Request request = requestMap.get(chatId);
            request.setCategory(category);
            requestMap.replace(chatId, request);
        }
    }

    public void changeRequestSource(long chatId, String source){

        if(!requestMap.containsKey(chatId)){
            Request request = new Request();
            request.setSource(source);
            requestMap.put(chatId, request);
        } else {
            Request request = requestMap.get(chatId);
            request.setSource(source);
            requestMap.replace(chatId, request);
        }
    }




    public boolean containsChatId(long chatId) { return requestMap.containsKey(chatId); }

    public Request getRequest(long chatId){
        return requestMap.get(chatId);
    }
}
