package com.mobilise.bookhub.exception;

import com.mobilise.bookhub.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.mobilise.bookhub.constants.Constants.INVALID_ENUM_ENTRY;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>("Validation Failed", errors, NOT_ACCEPTABLE.value()), NOT_ACCEPTABLE);
    }
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, NOT_FOUND.value()), NOT_FOUND);
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse<String>>handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(new ApiResponse<>("Access denied: Only administrators are allowed to perform this operation", null, HTTP_FORBIDDEN), FORBIDDEN);
    }
    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ApiResponse<String>> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, CONFLICT.value()), CONFLICT);
    }
    @ExceptionHandler(BookAlreadyCreatedException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ApiResponse<String>> handleBookAlreadyCreatedException(BookAlreadyCreatedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, CONFLICT.value()), CONFLICT);
    }

    @ExceptionHandler(BookCannotBeDeletedException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleBookCannotBeDeletedException(BookCannotBeDeletedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, BAD_REQUEST.value()), BAD_REQUEST);
    }
    @ExceptionHandler(BookOperationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleBookOperationException(BookOperationException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, BAD_REQUEST.value()), BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleBookNotAvailableException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, NOT_FOUND.value()), NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, BAD_REQUEST.value()), BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleInvalidCredentialException(InvalidCredentialException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, HTTP_BAD_REQUEST), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadableException() {
        return new ResponseEntity<>(new ApiResponse<>(INVALID_ENUM_ENTRY, null, HTTP_BAD_REQUEST), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, HTTP_BAD_REQUEST), BAD_REQUEST);
    }
    @ExceptionHandler(JwtParsingException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleJwtParsingException(JwtParsingException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, HTTP_BAD_REQUEST), BAD_REQUEST);
    }
    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleInvalidArgumentException(InvalidArgumentException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, BAD_REQUEST.value()), BAD_REQUEST);
    }
}
