package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Represents an exception that occurs during a book operation.
 *
 * @author codecharlan
 */
@Getter
public class BookOperationException extends RuntimeException {

    /**
     * Constructs a new instance of BookOperationException with the specified message.
     *
     * @param message the detail message
     */
    public BookOperationException(String message) {
        super(message);
        this.status = HttpStatus.NOT_ACCEPTABLE;
    }

    /**
     * The HTTP status code associated with this exception.
     */
    private final HttpStatus status;
}
