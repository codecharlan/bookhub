package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidArgumentException extends RuntimeException {
    private final HttpStatus status;
    public InvalidArgumentException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
