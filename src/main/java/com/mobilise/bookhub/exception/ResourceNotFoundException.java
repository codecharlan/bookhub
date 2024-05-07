package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * Represents an exception thrown when a requested resource is not found.
 *
 * @author codecharlan
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new instance of the ResourceNotFoundException with the specified message.
     *
     * @param message the error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_ACCEPTABLE;
    }

    /**
     * The HTTP status code indicating the type of the exception.
     */
    private final HttpStatus status;
}
