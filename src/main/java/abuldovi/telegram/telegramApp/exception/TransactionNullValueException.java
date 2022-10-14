package abuldovi.telegram.telegramApp.exception;

public class TransactionNullValueException extends RuntimeException{
    public TransactionNullValueException(String errorMessage) {
        super(errorMessage);
    }
}
