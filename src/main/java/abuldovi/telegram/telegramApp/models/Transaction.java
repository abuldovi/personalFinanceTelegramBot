package abuldovi.telegram.telegramApp.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.time.LocalDateTime;


@Entity
@Table(name = "finance")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "category")
    private String category;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;
    @Column(name = "source")
    private String source;

    @Min(value = 0, message = "Should be more than 0")
    @Max(value = Integer.MAX_VALUE, message = "Too big number")
    @Column(name = "value")
    private int value;

    public int getTransaction_id() {
        return transactionId;
    }

    public void setTransaction_id(int transaction_id) {
        this.transactionId = transaction_id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
