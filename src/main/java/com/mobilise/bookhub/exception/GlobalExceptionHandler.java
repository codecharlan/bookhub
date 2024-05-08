package com.mobilise.bookhub.exception;

import com.mobilise.bookhub.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.mobilise.bookhub.constants.Constants.INVALID_ENUM_ENTRY;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static org.springframework.http.HttpStatus.*;

/**
 * This class handles exceptions thrown in the application and returns appropriate HTTP responses.
 * The @ResponseStatus annotation is used to specify the HTTP status code that should be returned in the response.
 * @author charlancodes
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles {@link ConstraintViolationException} by creating a response with a status code of NOT_ACCEPTABLE.
     * The response contains a map of validation errors.
     *
     * @param e the {@link ConstraintViolationException} to handle
     * @return a {@link ResponseEntity} containing the validation errors
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>("Validation Failed", errors, NOT_ACCEPTABLE.value()), NOT_ACCEPTABLE);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getAllErrors().forEach(violation -> {
            String propertyPath = violation.getCode();
            String message = violation.getDefaultMessage();
            errors.put(propertyPath, message);
        });
        return new ResponseEntity<>(new ApiResponse<>("Validation Failed", errors, NOT_ACCEPTABLE.value()), NOT_ACCEPTABLE);
    }

    /**
     * Handles {@link UserNotFoundException} by creating a response with a status code of NOT_FOUND.
     * The response contains a message indicating that the user was not found.
     *
     * @param e the {@link UserNotFoundException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, NOT_FOUND.value()), NOT_FOUND);
    }

    /**
     * Handles {@link AccessDeniedException} by creating a response with a status code of FORBIDDEN.
     * The response contains a message indicating that the user does not have the right permission.
     *
     * @param e the {@link AccessDeniedException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(new ApiResponse<>("Access denied: Only administrators are allowed to perform this operation", null, HTTP_FORBIDDEN), FORBIDDEN);
    }

    /**
     * Handles {@link UserAlreadyExistException} by creating a response with a status code of CONFLICT.
     * The response contains a message indicating that the user already exists.
     *
     * @param e the {@link UserAlreadyExistException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ApiResponse<String>> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, CONFLICT.value()), CONFLICT);
    }

    /**
     * Handles {@link BookAlreadyCreatedException} by creating a response with a status code of CONFLICT.
     * The response contains a message indicating that the book already exists.
     *
     * @param e the {@link BookAlreadyCreatedException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(BookAlreadyCreatedException.class)
    @ResponseStatus(CONFLICT)
    public ResponseEntity<ApiResponse<String>> handleBookAlreadyCreatedException(BookAlreadyCreatedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, CONFLICT.value()), CONFLICT);
    }

    /**
     * Handles {@link BookCannotBeDeletedException} by creating a response with a status code of BAD_REQUEST.
     * The response contains a message indicating that the book cannot be deleted.
     *
     * @param e the {@link BookCannotBeDeletedException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(BookCannotBeDeletedException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleBookCannotBeDeletedException(BookCannotBeDeletedException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, BAD_REQUEST.value()), BAD_REQUEST);
    }

    /**
     * Handles {@link BookOperationException} by creating a response with a status code of BAD_REQUEST.
     * The response contains a message indicating the error in the book operation.
     *
     * @param e the {@link BookOperationException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(BookOperationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleBookOperationException(BookOperationException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, BAD_REQUEST.value()), BAD_REQUEST);
    }

    /**
     * Handles {@link ResourceNotFoundException} by creating a response with a status code of NOT_FOUND.
     * The response contains a message indicating that the resource was not found.
     *
     * @param e the {@link ResourceNotFoundException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ApiResponse<String>> handleBookNotAvailableException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, NOT_FOUND.value()), NOT_FOUND);
    }

    /**
     * Handles {@link IllegalArgumentException} by creating a response with a status code of BAD_REQUEST.
     * The response contains a message indicating the error in the input parameters.
     *
     * @param e the {@link IllegalArgumentException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, BAD_REQUEST.value()), BAD_REQUEST);
    }

    /**
     * Handles {@link InvalidCredentialException} by creating a response with a status code of BAD_REQUEST.
     * The response contains a message indicating the error in the input credentials.
     *
     * @param e the {@link InvalidCredentialException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleInvalidCredentialException(InvalidCredentialException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, HTTP_BAD_REQUEST), BAD_REQUEST);
    }

    /**
     * Handles {@link HttpMessageNotReadableException} by creating a response with a status code of BAD_REQUEST.
     * The response contains a message indicating that the input data is not in a format that can be processed.
     *
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadableException() {
        return new ResponseEntity<>(new ApiResponse<>(INVALID_ENUM_ENTRY, null, HTTP_BAD_REQUEST), BAD_REQUEST);
    }

    /**
     * Handles {@link IllegalStateException} by creating a response with a status code of BAD_REQUEST.
     * The response contains a message indicating the error in the application state.
     *
     * @param e the {@link IllegalStateException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, HTTP_BAD_REQUEST), BAD_REQUEST);
    }

    /**
     * Handles {@link JwtParsingException} by creating a response with a status code of BAD_REQUEST.
     * The response contains a message indicating the error in parsing the JWT token.
     *
     * @param e the {@link JwtParsingException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(JwtParsingException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleJwtParsingException(JwtParsingException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, HTTP_BAD_REQUEST), BAD_REQUEST);
    }

    /**
     * Handles {@link InvalidArgumentException} by creating a response with a status code of BAD_REQUEST.
     * The response contains a message indicating the error in the input parameters.
     *
     * @param e the {@link InvalidArgumentException} to handle
     * @return a {@link ResponseEntity} containing the error message
     */
    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponse<String>> handleInvalidArgumentException(InvalidArgumentException e) {
        return new ResponseEntity<>(new ApiResponse<>(e.getLocalizedMessage(), null, BAD_REQUEST.value()), BAD_REQUEST);
    }
}
