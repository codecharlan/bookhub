package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Represents an exception thrown when invalid credentials are provided.
 *
 * @author codecharlan
 */
@Getter
public class InvalidCredentialException extends RuntimeException {

    /**
     * Constructs an {@code InvalidCredentialException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public InvalidCredentialException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    /**
     * The HTTP status code associated with this exception.
     */
    private final HttpStatus status;
}
