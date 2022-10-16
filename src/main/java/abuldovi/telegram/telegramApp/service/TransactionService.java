package abuldovi.telegram.telegramApp.service;

import abuldovi.telegram.telegramApp.models.Transaction;
import abuldovi.telegram.telegramApp.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void deleteTransaction(Transaction transaction){
        transactionRepository.delete(transaction);
    }

    public List<Transaction> findAll(long chatId){
        return transactionRepository.findAllByChatId(chatId);
    }

    public List<Transaction> findByYear(long chatId, String string){
        return transactionRepository.getByYear(chatId, Integer.parseInt(string));
    }

    public Transaction findByTransactionId(int transactionId, long chatId){
        return transactionRepository.findFirstByTransactionIdAndChatId(transactionId, chatId);
    }

    public List<Transaction> findByYearAndCategory(long chatId, String year, String category){
        return transactionRepository.getByYearAndByCategory(chatId, category, Integer.parseInt(year));
    }

    public List<Transaction> findByYearAndSource(long chatId, String year, String source){
        System.out.println(year +  source);
        return transactionRepository.getByYearAndBySource(chatId, source, Integer.parseInt(year));
    }

    public Integer getSumByYearAndByCategory(long chatId, String string, String category){

        return transactionRepository.getSumByYearAndByCategory(chatId, category, Integer.parseInt(string));
    }

    public Integer getSumByYearAndBySource(long chatId, String string, String source){
        return transactionRepository.getSumByYearAndBySource(chatId, source, Integer.parseInt(string));
    }

    public Integer getSumByYear(long chatId, String string){
        return transactionRepository.getSumByYear(chatId, Integer.parseInt(string));
    }


    public List<Transaction> getByMonthAndYear(long chatId, String month, String year){
        return transactionRepository.getByMonthAndByYear(chatId, Integer.parseInt(month), Integer.parseInt(year));
    }

    public List<Transaction> getByMonthAndYearAndCategory(long chatId, String month, String year, String category){
        return transactionRepository.getByMonthAndByYearAndCategory(chatId, Integer.parseInt(month), Integer.parseInt(year), category);
    }

    public List<Transaction> getByMonthAndYearAndSource(long chatId, String month, String year, String source){
        return transactionRepository.getByMonthAndByYearAndSource(chatId, Integer.parseInt(month), Integer.parseInt(year), source);
    }

    public List<Integer> findMonthsByYear(long chatId, String year) {return  transactionRepository.getMonthsByYear(chatId, Integer.parseInt(year));}

    public Integer getSumByMonth(long chatId, String month, String year){
        return transactionRepository.getSumByMonth(chatId, Integer.parseInt(month), Integer.parseInt(year));
    }

    public Integer getSumByMonthAndByYearAndByCategory(long chatId, String month, String year, String category){
        return transactionRepository.getSumByMonthAndByYearAndByCategory(chatId, Integer.parseInt(year), Integer.parseInt(month), category);
    }

    public Integer getSumByMonthAndByYearAndSource(long chatId, String month, String year, String source){
        return transactionRepository.getSumByMonthAndByYearAndBySource(chatId, Integer.parseInt(year), Integer.parseInt(month), source);
    }

    public List<Transaction> getByMonthAndByYearAndSource(long chatId, String month, String year, String source){
        return transactionRepository.getByMonthAndByYearAndSource(chatId, Integer.parseInt(month), Integer.parseInt(year), source);
    }

    public List<Integer> findYears(long chatId){
        return transactionRepository.getYears(chatId);
    }

    @Transactional
    public void save(long chatId, Transaction transaction){
        transaction.setChatId(chatId);
        transaction.setTimestamp(LocalDateTime.now());
        System.out.println(transaction.getValue());
        transactionRepository.save(transaction);
    }

    @Transactional
    public void save(Transaction transaction){
        transactionRepository.save(transaction);
    }


}
