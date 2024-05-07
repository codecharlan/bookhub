package com.mobilise.bookhub.controller;

import com.mobilise.bookhub.dto.request.BookRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.BookResponseDto;
import com.mobilise.bookhub.exception.BookOperationException;
import com.mobilise.bookhub.exception.InvalidArgumentException;
import com.mobilise.bookhub.exception.ResourceNotFoundException;
import com.mobilise.bookhub.security.JwtService;
import com.mobilise.bookhub.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.mobilise.bookhub.constants.Constants.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * * mobilise-book-hub
 * * BookController.java
 * * May 05, 2024,
 * Controller for managing book operations.
 *
 * @author charlancodes
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(BASE_BOOK_API_URL)
public class BookController {
    /**
     * Service for managing book operations.
     */
    private final BookService bookService;
    /**
     * Service for managing JWT tokens.
     */
    private final JwtService jwtService;
    private final static Logger logger = LoggerFactory.getLogger(BookController.class);

    /**
     * Secured method for creating a new book.
     *
     * @param authorizationHeader The authorization header containing the JWT token.
     * @param newBook             The new book details.
     * @return A response containing the created book.
     * @throws AccessDeniedException if User does not have Admin permission to access the method.
     */
    @Secured("ADMINISTRATOR")
    @PostMapping(CREATE_URL)
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook(@RequestHeader(name = AUTHORIZATION_HEADER) String authorizationHeader, @Valid @RequestBody BookRequestDto newBook) {
        try {
            Map<String, String> userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
            String administrator = userDetails.get("email");
            ApiResponse<BookResponseDto> response = bookService.createBook(administrator, newBook);
            HttpStatus httpStatus = HttpStatus.valueOf(response.status());
            return new ResponseEntity<>(response, httpStatus);
        } catch (AccessDeniedException e) {
            logger.error("Error creating book: {}", e.getMessage());
            return new ResponseEntity<>(null, BAD_REQUEST);
        }
    }

    /**
     * Secured method for editing an existing book.
     *
     * @param authorizationHeader The authorization header containing the JWT token.
     * @param id                  The id of the book to be edited.
     * @param updatedBook         The updated book details.
     * @return A response containing the updated book.
     */
    @Secured("ADMINISTRATOR")
    @PutMapping(EDIT_URL)
    public ResponseEntity<ApiResponse<BookResponseDto>> editBook(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
                                                                 @PathVariable Long id,
                                                                 @RequestBody BookRequestDto updatedBook) {
        try {
            String token = authorizationHeader.substring(7);
            String userEmail = jwtService.parseTokenClaims(token).get("email");
            ApiResponse<BookResponseDto> response = bookService.editBook(userEmail, id, updatedBook);
            HttpStatus httpStatus = HttpStatus.valueOf(response.status());
            return new ResponseEntity<>(response, httpStatus);
        } catch (AccessDeniedException e) {
            logger.error("Error creating book: {}", e.getMessage());
            return new ResponseEntity<>(null, BAD_REQUEST);
        }
    }

    @GetMapping(ID_URL)
    public ResponseEntity<ApiResponse<BookResponseDto>> getBookById(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader, @PathVariable Long id) {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtService.parseTokenClaims(token).get("email");
        ApiResponse<BookResponseDto> response = bookService.getBookById(userEmail, id);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
    @GetMapping(SEARCH_URL)
    public ResponseEntity<ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>>> searchBooksByTitleOrAuthor(
            @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchTerm", required = false) String searchTerm) {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtService.parseTokenClaims(token).get("email");
        ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>> response =
                bookService.searchBooksByTitleOrAuthor(userEmail, pageNumber, pageSize, searchTerm);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Method for retrieving all books.
     *
     * @param authorizationHeader The authorization header containing the JWT token.
     * @param pageNumber          The page number of the results.
     * @param pageSize            The number of results per page.
     * @param sortBy              The field to sort by.
     * @param sortOrder           The order of sorting (ascending or descending).
     * @param searchTerm          The search term for filtering the results.
     * @return A response containing the list of books and pagination information.
     */
    @GetMapping(GET_ALL_URL)
    public ResponseEntity<ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>>> retrieveAllBooks(
            @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(required = false) String searchTerm) {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtService.parseTokenClaims(token).get("email");

        ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>> response =
                bookService.getAllBooks(userEmail, pageNumber, pageSize, sortBy, sortOrder, searchTerm);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Secured method for deleting a book.
     *
     * @param authorizationHeader The authorization header containing the JWT token.
     * @param id                  The id of the book to be deleted.
     * @return A response containing a message indicating the success or failure of the operation.
     */
    @Secured("ADMINISTRATOR")
    @DeleteMapping(DELETE_URL)
    public ResponseEntity<ApiResponse<String>> deleteBook(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader, @PathVariable Long id) {
        try {
            String token = authorizationHeader.substring(7);
            String userEmail = jwtService.parseTokenClaims(token).get("email");
            ApiResponse<String> response = bookService.deleteBook(userEmail, id);
            HttpStatus httpStatus = HttpStatus.valueOf(response.status());
            return new ResponseEntity<>(response, httpStatus);
        } catch (AccessDeniedException e) {
            logger.error("Error creating book: {}", e.getMessage());
            return new ResponseEntity<>(null, BAD_REQUEST);
        }
    }

    /**
     * Method for borrowing a book.
     *
     * @param authorizationHeader The authorization header containing the JWT token.
     * @param borrowCount         The number of books to be borrowed.
     * @param bookId              The id of the book to be borrowed.
     * @return A response containing the borrowed book.
     */
    @PostMapping(BORROW_BOOK_URL)
    public ResponseEntity<ApiResponse<BookResponseDto>> borrowBook(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
                                                                   @RequestParam Integer borrowCount,
                                                                   @PathVariable Long bookId)
            throws InvalidArgumentException, BookOperationException, ResourceNotFoundException {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtService.parseTokenClaims(token).get("email");
        ApiResponse<BookResponseDto> response = bookService.borrowBook(bookId, userEmail, borrowCount);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Method for returning a book.
     *
     * @param authorizationHeader The authorization header containing the JWT token.
     * @param returnCount         The number of books to be returned.
     * @param bookId              The id of the book to be returned.
     * @return A response containing the returned book.
     */
    @PostMapping(RETURN_BOOK_URL)
    public ResponseEntity<ApiResponse<BookResponseDto>> returnBook(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
                                                                   @RequestParam Integer returnCount,
                                                                   @PathVariable Long bookId)
            throws InvalidArgumentException, BookOperationException, ResourceNotFoundException {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtService.parseTokenClaims(token).get("email");
        ApiResponse<BookResponseDto> response = bookService.returnBook(userEmail, bookId, returnCount);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }

    /**
     * Method for purchasing a book.
     *
     * @param authorizationHeader The authorization header containing the JWT token.
     * @param purchaseCount       The number of books to be purchased.
     * @param bookId              The id of the book to be purchased.
     * @return A response containing the purchased book.
     */
    @PostMapping(PURCHASE_BOOK_URL)
    public ResponseEntity<ApiResponse<BookResponseDto>> purchaseBook(@RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
                                                                     @RequestParam Integer purchaseCount,
                                                                     @PathVariable Long bookId)
            throws InvalidArgumentException, BookOperationException, ResourceNotFoundException {
        String token = authorizationHeader.substring(7);
        String userEmail = jwtService.parseTokenClaims(token).get("email");
        ApiResponse<BookResponseDto> response = bookService.purchaseBook(bookId, userEmail, purchaseCount);
        HttpStatus httpStatus = HttpStatus.valueOf(response.status());
        return new ResponseEntity<>(response, httpStatus);
    }
}