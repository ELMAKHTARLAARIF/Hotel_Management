package exception;

public class EmailFormatException extends RuntimeException {

    public EmailFormatException(String message) {
        super(message);
    }

    public EmailFormatException() {
        super("Invalid email format.");
    }
}