package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class InvalidCredentialException extends RuntimeException {
    private final HttpStatus status;
    public InvalidCredentialException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
