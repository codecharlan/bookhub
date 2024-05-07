package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Custom exception for JWT parsing errors.
 *
 * @author codecharlan
 * @version 1.0.0
 */
@Getter
public class JwtParsingException extends RuntimeException {

    /**
     * Constructs a JwtParsingException with the specified error message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public JwtParsingException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    /**
     * The HTTP status code associated with this exception.
     */
    private final HttpStatus status;
}
