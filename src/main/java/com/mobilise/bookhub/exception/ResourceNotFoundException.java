package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final HttpStatus status;
    public ResourceNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_ACCEPTABLE;
    }
}
