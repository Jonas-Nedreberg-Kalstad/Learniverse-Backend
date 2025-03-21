package no.ntnu.idata2306.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Global exception handler for handling validation and custom exceptions across the application.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_CODE = "errorCode";
    private static final String DETAILS = "details";
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";

    /**
     * Handles MethodArgumentNotValidException and returns a response with validation error messages.
     * This method collects all validation errors for each field and returns them in a list.
     *
     * @param ex the MethodArgumentNotValidException thrown when validation fails
     * @return ResponseEntity containing a map of field names and their corresponding error messages, with HTTP status 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.computeIfAbsent(error.getField(), k -> new ArrayList<>()).add(error.getDefaultMessage());
        }
        log.error("Bad Request: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles EntityNotFoundException and returns a response with an error message.
     *
     * @param ex the EntityNotFoundException thrown when an entity is not found
     * @return ResponseEntity containing the error message, with HTTP status 404 (Not Found)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles HttpMediaTypeNotSupportedException and returns a response with an error message.
     *
     * @param ex the HttpMediaTypeNotSupportedException thrown when the media type is not supported
     * @return ResponseEntity containing the error details, with HTTP status 415 (Unsupported Media Type)
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<Map<String, Object>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put(TIMESTAMP, LocalDateTime.now());
        errorDetails.put(MESSAGE, "Unsupported media type. Please ensure the Content-Type header is set to 'application/json', and that data sent is of correct format.");
        errorDetails.put(DETAILS, ex.getMessage());
        errorDetails.put(ERROR_CODE, HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString());

        log.error("Unsupported media type: ", ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Handles BKTreeException and returns a response with an error message.
     *
     * @param ex the BKTreeException thrown when BKTree initialization fails due to no data
     * @return ResponseEntity containing the error message, with HTTP status 204 (No Content)
     */
    @ExceptionHandler(BKTreeException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, Object>> handleBKTreeException(BKTreeException ex) {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put(TIMESTAMP, LocalDateTime.now());
        errorDetails.put(MESSAGE, "BKTree initialization failed. No data available. Please try again later.");
        errorDetails.put(DETAILS, ex.getMessage());
        errorDetails.put(ERROR_CODE, HttpStatus.NO_CONTENT.toString());

        log.error("BKTree initialization error: ", ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.NO_CONTENT);
    }

    /**
     * Handles all other exceptions and returns a response with an error message.
     *
     * @param ex the Exception thrown
     * @return ResponseEntity containing the error message, with HTTP status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put(TIMESTAMP, LocalDateTime.now());
        errorDetails.put(MESSAGE, "An unexpected error occurred. Please try again later.");
        errorDetails.put(DETAILS, ex.getMessage());
        errorDetails.put(ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR.toString());

        log.error("Internal server error details: {}, error: ", errorDetails, ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
