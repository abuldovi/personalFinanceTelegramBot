package abuldovi.telegram.telegramApp.util;

import abuldovi.telegram.telegramApp.models.Transaction;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class TransactionState {
    private final Map<Long, Transaction> transactionStateMap = new LinkedHashMap<>();

    public void changeTransactionState(long chatId, Transaction transaction){
        if(!transactionStateMap.containsKey(chatId)){
            transactionStateMap.put(chatId, transaction);
        } else{
            transactionStateMap.replace(chatId, transaction);
        }
    }

    public Transaction getTransactionState(long chatId){
        return transactionStateMap.get(chatId);
    }
    public void removeTransactionState(long chatId){
        transactionStateMap.remove(chatId);
    }

}
