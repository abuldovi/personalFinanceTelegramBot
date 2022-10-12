package abuldovi.telegram.telegramApp.repositories;

import abuldovi.telegram.telegramApp.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByChatId(long chatId);

    @Query("SELECT sum(t.value) from Transaction t where t.chatId = ?1 AND year(t.timestamp) =?2")
    Integer getSumByYear(long chatId, int data);

    @Query("SELECT t from Transaction t where t.chatId = ?1 AND year(t.timestamp) =?2")
    List<Transaction> getByYear(long chatId, int year);

    @Query("SELECT t from Transaction t where t.chatId = ?1 AND t.category = ?2 AND year(t.timestamp) =?3")
    List<Transaction> getByYearAndByCategory(long chatId, String category, int year);

    @Query("SELECT t from Transaction t where t.chatId = ?1 AND t.source = ?2 AND year(t.timestamp) =?3")
    List<Transaction> getByYearAndBySource(long chatId, String source, int year);

    @Query("SELECT sum(t.value) from Transaction t where t.chatId = ?1 AND t.category = ?2 AND year(t.timestamp) =?3")
    Integer getSumByYearAndByCategory(long chatId, String category, int year);

    @Query("SELECT sum(t.value) from Transaction t where t.chatId = ?1 AND t.source = ?2 AND year(t.timestamp) =?3")
    Integer getSumByYearAndBySource(long chatId, String source, int year);

    @Query("SELECT sum(t.value) from Transaction t where t.chatId = ?1 AND year(t.timestamp) =?2 AND month(t.timestamp) =?3 AND t.source = ?4")
    Integer getSumByMonthAndByYearAndBySource(long chatId, int year, int month, String source);

    @Query("SELECT sum(t.value) from Transaction t where t.chatId = ?1 AND year(t.timestamp) =?2 AND month(t.timestamp) =?3 AND t.category = ?4")
    Integer getSumByMonthAndByYearAndByCategory(long chatId, int year, int month, String category);

    @Query("SELECT sum(t.value) from Transaction t where t.chatId = ?1 AND month(t.timestamp) =?2 AND year(t.timestamp) =?3")
    Integer getSumByMonth(long chatId, int data, int year);

    @Query("SELECT t from Transaction t where t.chatId = ?1 AND month(t.timestamp) =?2 AND year(t.timestamp) =?3")
    List<Transaction> getByMonthAndByYear(long chatId, int data, int year);

    @Query("SELECT t from Transaction t where t.chatId = ?1 AND month(t.timestamp) =?2 AND year(t.timestamp) =?3 AND t.category = ?4")
    List<Transaction> getByMonthAndByYearAndCategory(long chatId, int data, int year, String category);

    @Query("SELECT t from Transaction t where t.chatId = ?1 AND month(t.timestamp) =?2 AND year(t.timestamp) =?3 AND t.source = ?4")
    List<Transaction> getByMonthAndByYearAndSource(long chatId, int data, int year, String source);

    @Query("SELECT DISTINCT EXTRACT(year from timestamp) from Transaction where chatId = ?1")
    List<Integer> getYears(long chatId);

    @Query("SELECT DISTINCT EXTRACT(month from timestamp) from Transaction where chatId = ?1 AND year(timestamp) =?2")
    List<Integer> getMonthsByYear(long chatId, int year);


}
