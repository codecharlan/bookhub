package com.mobilise.bookhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class JwtParsingException extends RuntimeException {
    private final HttpStatus status;

    public JwtParsingException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
