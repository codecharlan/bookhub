package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class BookOperationException extends RuntimeException {
    private final HttpStatus status;
    public BookOperationException(String message) {
        super(message);
        this.status = HttpStatus.NOT_ACCEPTABLE;
    }

}
