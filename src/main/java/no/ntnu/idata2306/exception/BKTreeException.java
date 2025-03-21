package no.ntnu.idata2306.exception;

/**
 * BKTreeException is a custom exception class used to handle errors related to BKTree operations.
 */
public class BKTreeException extends RuntimeException {
    /**
     * Constructs a new BKTreeException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BKTreeException(String message) {
        super(message);
    }
}
