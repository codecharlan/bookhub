package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class BookAlreadyCreatedException extends RuntimeException {
    private final HttpStatus status;
    public BookAlreadyCreatedException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }
}
