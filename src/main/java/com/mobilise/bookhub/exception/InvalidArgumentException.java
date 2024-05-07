package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Represents an exception thrown when an invalid argument is provided.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@Getter
public class InvalidArgumentException extends RuntimeException {

    /**
     * Constructs an {@code InvalidArgumentException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public InvalidArgumentException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    /**
     * The HTTP status code associated with this exception.
     */
    private final HttpStatus status;
}
