package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Represents an exception that is thrown when a book with the same title already exists in the system.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@Getter
public class BookAlreadyCreatedException extends RuntimeException {

    /**
     * Constructs a new instance of the {@code BookAlreadyCreatedException} with the specified error message.
     *
     * @param message the detail message
     */
    public BookAlreadyCreatedException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    /**
     * The HTTP status code for this exception.
     */
    private final HttpStatus status;
}
