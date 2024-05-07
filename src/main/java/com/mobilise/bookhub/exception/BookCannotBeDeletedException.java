package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Represents an exception that is thrown when a book cannot be deleted.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@Getter
public class BookCannotBeDeletedException extends RuntimeException {

    /**
     * Constructs a new instance of the {@code BookCannotBeDeletedException} with the specified error message.
     *
     * @param message the error message
     */
    public BookCannotBeDeletedException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    /**
     * The HTTP status code associated with this exception.
     */
    private final HttpStatus status;
}
