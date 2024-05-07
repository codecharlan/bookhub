package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class UserAlreadyExistException extends RuntimeException {
    private final HttpStatus status;
    public UserAlreadyExistException(String message) {
        super(message);
        this.status = HttpStatus.ALREADY_REPORTED;
    }
}
