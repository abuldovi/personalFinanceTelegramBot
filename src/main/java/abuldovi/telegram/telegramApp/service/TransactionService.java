package abuldovi.telegram.telegramApp.service;

import abuldovi.telegram.telegramApp.models.Transaction;
import abuldovi.telegram.telegramApp.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findAll(long chatId){
        return transactionRepository.findAllByChatId(chatId);
    }

    public List<Transaction> findByYear(long chatId, String string){
        return transactionRepository.getByYear(chatId, Integer.parseInt(string));
    }

    public Integer getSumByYear(long chatId, String string){
        return transactionRepository.getSumByYear(chatId, Integer.parseInt(string));
    }

    public List<Transaction> getByMonthAndYear(long chatId, String month, String year){
        return transactionRepository.getByMonthAndByYear(chatId, Integer.parseInt(month), Integer.parseInt(year));
    }

    public List<Integer> findMonthsByYear(long chatId, String year) {return  transactionRepository.getMonthsByYear(chatId, Integer.parseInt(year));}

    public Integer getSumByMonth(long chatId, String month, String year){
        return transactionRepository.getSumByMonth(chatId, Integer.parseInt(month), Integer.parseInt(year));
    }



    public List<Integer> findYears(long chatId){
        return transactionRepository.getYears(chatId);
    }

    @Transactional
    public Transaction save(long chatId, Transaction transaction){
        transaction.setChatId(chatId);
        transaction.setTimestamp(new Date());
        System.out.println(transaction.getValue());
        return transactionRepository.save(transaction);
    }


}
