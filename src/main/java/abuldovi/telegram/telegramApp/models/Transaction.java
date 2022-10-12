package abuldovi.telegram.telegramApp.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.Date;

@Entity
@Table(name = "finance")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transaction_id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "category")
    private String category;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    @Column(name = "source")
    private String source;

    @Min(value = 0, message = "Should be more than 0")
    @Max(value = Integer.MAX_VALUE, message = "Too big number")
    @Column(name = "value")
    private int value;

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
