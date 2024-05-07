package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class BookCannotBeDeletedException extends RuntimeException {
    private final HttpStatus status;
    public BookCannotBeDeletedException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
