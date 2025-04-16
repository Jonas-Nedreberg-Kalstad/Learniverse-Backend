package no.ntnu.idata2306.exception;

/**
 * ReceiptGenerationException is a custom exception class used to handle errors related to receipt generation.
 */
public class ReceiptGenerationException extends RuntimeException {
    /**
     * Constructs a new ReceiptGenerationException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ReceiptGenerationException(String message) {
        super(message);
    }
}
