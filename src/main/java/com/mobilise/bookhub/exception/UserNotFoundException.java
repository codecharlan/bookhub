package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Represents an exception thrown when a user is not found.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@Getter
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a UserNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public UserNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

    /**
     * The HTTP status code for this exception.
     */
    private final HttpStatus status;
}
