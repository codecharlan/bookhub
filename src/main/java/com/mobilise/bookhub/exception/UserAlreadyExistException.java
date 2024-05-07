package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Represents an exception that is thrown when a user already exists in the system.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@Getter
public class UserAlreadyExistException extends RuntimeException {

    /**
     * Constructs a new instance of {@code UserAlreadyExistException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public UserAlreadyExistException(String message) {
        super(message);
        this.status = HttpStatus.ALREADY_REPORTED;
    }

    /**
     * The HTTP status code that corresponds to the exception.
     */
    private final HttpStatus status;
}
