package no.ntnu.idata2306.exception;

/**
 * CardTokenHashException is a custom exception class used to handle errors related to card token hashing operations.
 */
public class CardTokenHashException extends Exception {

    /**
     * Constructs a new CardTokenHashException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause of the exception.
     */
    public CardTokenHashException(String message, Throwable cause) {
        super(message, cause);
    }
}
