package com.mobilise.bookhub.services.serviceImpl;


import com.mobilise.bookhub.dto.request.BookRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.BookResponseDto;
import com.mobilise.bookhub.entity.*;
import com.mobilise.bookhub.enums.TransactionType;
import com.mobilise.bookhub.exception.*;
import com.mobilise.bookhub.repository.*;
import com.mobilise.bookhub.services.BookService;
import com.mobilise.bookhub.utils.BookUtils;
import com.mobilise.bookhub.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.mobilise.bookhub.enums.TransactionStatus.COMPLETED;
import static com.mobilise.bookhub.enums.TransactionType.*;
import static java.math.BigDecimal.ZERO;
import static java.net.HttpURLConnection.*;
/**
 * Service class for managing books.
 *
 * @author codecharlan
 * @version  1. 0. 0
 */

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final TransactionRepository transactionRepository;
    private final DtoMapper dtoMapper;
    private final BookUtils bookUtils;
    private final static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    /**
     * Create a new book.
     *
     * @param email the email of the user creating the book
     * @param newBook the details of the new book
     * @return the created book
     */
    @Override
    public ApiResponse<BookResponseDto> createBook(String email, BookRequestDto newBook) {
        findUserByEmail(email);
        try {
            checkForDuplicateBook(newBook.title(), newBook.author().getName());
            Book createdBook = dtoMapper.createNewBook(newBook);
            Book savedBook = bookRepository.save(createdBook);
            BookResponseDto response = dtoMapper.createBookResponse(savedBook);
            logger.info("Book created successfully (ID: {})", savedBook.getId());
            return new ApiResponse<>("Book created successfully (ID: " + savedBook.getId() + ")",
                    response, HttpStatus.CREATED.value());
        } catch (Exception e) {
            logger.error("Error creating book: {}", e.getMessage());
            throw new BookOperationException("An error occurred while creating " + newBook.title() + ": " + e.getLocalizedMessage());
        }
    }
    /**
     * Find a user by email.
     *
     * @param email the email of the user to find
     * @return the user if found, or throws a {@link UserNotFoundException} if not found
     */
    User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("The requested user could not be found with this email: " + email));
    }
    /**
     * Check if a book with the same title and author already exists.
     *
     * @param title the title of the book to check
     * @param authorsName the name of the author to check
     * @return {@code true} if a book with the same title and author already exists, {@code false} otherwise
     */
    boolean checkForDuplicateBook(String title, String authorsName) {
        Optional<Book> bookCheck = bookRepository.findByTitleAndAuthor_Name(title, authorsName);
        if (bookCheck.isPresent()) {
            throw new BookAlreadyCreatedException("A book with the same title and author already exists: "
                    + bookCheck.get().getTitle() + " by " + bookCheck.get().getAuthor().getName());
        }
        return false;
    }

    /**
     * Edit a book.
     *
     * @param email the email of the user editing the book
     * @param id the ID of the book to edit
     * @param updatedBook the details of the updated book
     * @return the updated book
     * @throws Exception if an error occurs while editing the book
     */
    @Override
    public ApiResponse<BookResponseDto> editBook(String email, Long id, BookRequestDto updatedBook) {
        findUserByEmail(email);
        try {
            Book retrievedBook = findBookById(id);

            Optional<Author> retrievedAuthor = authorRepository.findByEmailAddress(retrievedBook.getAuthor().getEmailAddress());
            Optional<Publisher> retrievedPublisher = publisherRepository.findByName(retrievedBook.getPublisher().getName());

            retrievedBook.setAuthor(retrievedAuthor.orElseGet(() -> updateAuthor(updatedBook)));
            retrievedBook.setPublisher(retrievedPublisher.orElseGet(() -> updatePublisher(updatedBook)));
            retrievedBook.setTitle(updatedBook.title());
            retrievedBook.setIsbn(updatedBook.isbn());
            retrievedBook.setEdition(updatedBook.edition());
            retrievedBook.setStatus(updatedBook.status());
            retrievedBook.setDescription(updatedBook.description());
            retrievedBook.setGenre(updatedBook.genre());
            retrievedBook.setTotalCopies(updatedBook.totalCopies());
            retrievedBook.setPublicationYear(updatedBook.publicationYear());
            retrievedBook.setCoAuthors(updatedBook.coAuthors());
            retrievedBook.setUnitPriceOfBook(updatedBook.unitPriceOfBook());
            retrievedBook.setTotalCopies(updatedBook.totalCopies());

            checkForDuplicateBook(retrievedBook.getTitle(), retrievedBook.getAuthor().getName());

            Book savedBook = bookRepository.save(retrievedBook);
            BookResponseDto response = dtoMapper.createBookResponse(savedBook);
            logger.info("Book edited successfully (ID: {})", savedBook.getId());
            return new ApiResponse<>("Book Edited Successfully", response, HTTP_NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error editing book (ID: {}): {}", id, e.getMessage());
            throw new BookOperationException("an error occurred while editing book (ID: {})" + id + e.getLocalizedMessage());
        }
    }
    /**
     * Updates the author of a book.
     *
     * @param authorDto the details of the new author
     * @return the updated author
     */
    private Author updateAuthor(BookRequestDto authorDto) {
        Author author = new Author(authorDto.author().getName(), authorDto.author().getBiography(),
                authorDto.author().getNationality(), authorDto.author().getEmailAddress());
        return authorRepository.save(author);
    }
    /**
     * Updates the publisher of a book.
     *
     * @param publisherDto the details of the new publisher
     * @return the updated publisher
     */
    private Publisher updatePublisher(BookRequestDto publisherDto) {
        Publisher publisher = new Publisher(publisherDto.publisher().getName(),
                publisherDto.publisher().getLocation(), publisherDto.publisher().getContactInformation());
        return publisherRepository.save(publisher);
    }

    /**
     * Get all books from the database.
     *
     * @param email the email of the user making the request
     * @param pageNumber the page number of the results to return
     * @param pageSize the number of results per page
     * @param sortBy the field to sort the results by
     * @param sortOrder the order of the sort (ascending or descending)
     * @param searchTerm the term to search for in the book titles or authors' names
     * @return a wrapper containing the list of books and pagination information
     */
    @Override
    public ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>> getAllBooks(String email,
                                                                               int pageNumber,
                                                                               int pageSize,
                                                                               String sortBy,
                                                                               String sortOrder,
                                                                               String searchTerm) {
        findUserByEmail(email);
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortBy.toLowerCase());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Book> bookPage;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            bookPage = bookRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);
        } else {
            bookPage = bookRepository.findAll(pageable);
        }
        List<BookResponseDto> responses = new ArrayList<>();
        for (Book book : bookPage.getContent()) {
            BookResponseDto bookResponse = dtoMapper.createBookResponse(book);
            responses.add(bookResponse);
        }
        ApiResponse.Wrapper<List<BookResponseDto>> wrapper = new ApiResponse.Wrapper<>(
                responses,
                bookPage.getNumber(),
                bookPage.getSize(),
                bookPage.getTotalPages(),
                bookPage.getTotalElements()
        );

        return new ApiResponse<>("Books Fetched Successfully", wrapper, HTTP_OK);
    }
    /**
     * Get a book by its ID.
     *
     * @param email the email of the user making the request
     * @param id the ID of the book to retrieve
     * @return a wrapper containing the book details and book information
     */
    @Override
    public ApiResponse<BookResponseDto> getBookById(String email, Long id) {
        findUserByEmail(email);
        Book book = findBookById(id);
        BookResponseDto response = dtoMapper.createBookResponse(book);
        return new ApiResponse<>("Book retrieved successfully (ID: " + response.id() + ")", response, HTTP_OK);
    }
    /**
     * Finds a book by its ID.
     *
     * @param id the ID of the book to retrieve
     * @return the book if found, or throws a {@link ResourceNotFoundException} if not found
     */
    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Book not found for id " + id));
    }
    /**
     * Deletes a book from the database.
     *
     * @param email the email of the user making the request
     * @param id the ID of the book to delete
     * @return a wrapper containing a success message if the book is successfully deleted, or an error message if the book cannot be deleted
     * throws BookNotFoundException if the book with the given ID is not found
     * throws BookCannotBeDeletedException if the book has borrowed copies and cannot be deleted
     * throws BookOperationException if an error occurs while deleting the book
     */
    @Override
    public ApiResponse<String> deleteBook(String email, Long id) {
        findUserByEmail(email);
        try {
            Book book = findBookById(id);
            if (book.getBorrowedCopies() == null || book.getBorrowedCopies() == 0) {
                bookRepository.delete(book);
                logger.info("Book deleted successfully (ID: {})", id);
                return new ApiResponse<>("Book Deleted Successfully", "Deleted", HTTP_NO_CONTENT);
            } else if (book.getBorrowedCopies() > 0) {
                throw new BookCannotBeDeletedException("Cannot delete book with borrowed copies. Please return all copies before deletion.");
            }
        } catch (Exception e) {
            logger.error("Error deleting book (ID: {}): {}", id, e.getMessage());
            throw new BookOperationException("Error deleting  book (ID: {}): " + id + " " + e.getLocalizedMessage());
        }
        return new ApiResponse<>("Error Deleting Book", null, HTTP_BAD_REQUEST);
    }
    /**
     * Searches for books by their title or author's name.
     *
     * @param email the email of the user making the request
     * @param pageNumber the page number of the results to return
     * @param pageSize the number of results per page
     * @param searchTerm the term to search for in the book titles or authors' names
     * @return a wrapper containing the list of books and pagination information
     * @throws BookOperationException if an error occurs while searching books
     */
    @Override
    public ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>> searchBooksByTitleOrAuthor(String email,
                                                                                              int pageNumber,
                                                                                              int pageSize,
                                                                                              String searchTerm) {
        findUserByEmail(email);
        try {
            Sort sort = Sort.by(Sort.Direction.ASC, "title");
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

            Page<Book> bookPage;
            if (searchTerm != null && !searchTerm.isEmpty()) {
                bookPage = bookRepository.findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(searchTerm, searchTerm, pageable);
            } else {
                bookPage = bookRepository.findAll(pageable);
            }
            List<BookResponseDto> searchResponses = new LinkedList<>();
            for (Book eachbook : bookPage.getContent()) {
                BookResponseDto bookResponse = dtoMapper.createBookResponse(eachbook);
                searchResponses.add(bookResponse);
            }
            ApiResponse.Wrapper<List<BookResponseDto>> wrapper = new ApiResponse.Wrapper<>(
                    searchResponses,
                    bookPage.getNumber(),
                    bookPage.getSize(),
                    bookPage.getTotalPages(),
                    bookPage.getTotalElements()
            );

            return new ApiResponse<>("Books Fetched Successfully", wrapper, HTTP_OK);
        } catch (Exception e) {
            logger.error("Error searching books: {}", e.getMessage());
            throw new BookOperationException("An error occurred while searching books: " + e.getLocalizedMessage());
        }
    }
    /**
     * Borrow a book.
     *
     * @param bookId the ID of the book to borrow
     * @param email the email of the user borrowing the book
     * @param borrowCount the number of copies to borrow
     * @return a wrapper containing the borrowed book and a success message if the borrow is successful, or an error message if the borrow cannot be completed
     * @throws InvalidArgumentException if the borrow count is less than or equal to 0
     * @throws BookOperationException if an error occurs while borrowing the book
     * @throws ResourceNotFoundException if the book with the given ID is not found
     */
    @Override
    public ApiResponse<BookResponseDto> borrowBook(Long bookId, String email, Integer borrowCount)
            throws InvalidArgumentException, BookOperationException, ResourceNotFoundException {
        User borrower = findUserByEmail(email);
        try {
            if (borrowCount <= 0) {
                throw new InvalidArgumentException("Borrow count must be greater than 0");
            }
            Book requestedBook = findBookById(bookId);
            if (borrowCount > requestedBook.getTotalCopies()) {
                throw new BookOperationException("Cannot borrow more copies than available copies " + requestedBook.getTotalCopies());
            }
            bookUtils.updateBookAvailability(requestedBook, borrowCount, BORROW);
            Book updatedBook = findBookById(bookId);
            Transaction saveTransaction = Transaction.builder()
                    .user(borrower)
                    .type(BORROW)
                    .status(COMPLETED)
                    .book(updatedBook)
                    .amount(ZERO)
                    .build();
            transactionRepository.save(saveTransaction);
            BookResponseDto response = dtoMapper.createBookResponse(updatedBook);
            logger.info("Successfully borrowed {} copies of book (ID: {}) by user {} ", borrowCount, requestedBook.getId(), borrower.getEmail());
            return new ApiResponse<>("Successfully borrowed " + borrowCount + " copies of " + requestedBook.getTitle(), response, HTTP_OK);
        } catch (InvalidArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage());
            throw e;
        } catch (BookOperationException e) {
            logger.error("Book operation error: {}", e.getMessage());
            throw e;
        } catch (ResourceNotFoundException e) {
            logger.error("Book not found error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error borrowing book (ID: {}): {} ", bookId, e.getMessage());
            throw new BookOperationException("Error Occurred while borrowing book (ID: " + bookId + ")");
        }
    }

    /**
     * Returns a book by updating its availability and creating a transaction record.
     *
     * @param email       The email of the user returning the book.
     * @param bookId      The ID of the book to be returned.
     * @param returnCount The number of copies of the book to be returned.
     * @return An ApiResponse containing information about the returned book.
     * @throws InvalidArgumentException  if the return count is less than or equal to 0.
     * @throws BookOperationException    if the return count exceeds the number of borrowed copies or an error occurs during the operation.
     * @throws ResourceNotFoundException if the book or user is not found.
     */
    @Override
    public ApiResponse<BookResponseDto> returnBook(String email, Long bookId, int returnCount)
            throws InvalidArgumentException, BookOperationException, ResourceNotFoundException {
        User borrower = findUserByEmail(email);
        try {
            if (returnCount <= 0) {
                throw new InvalidArgumentException("Return count must be greater than 0");
            }
            Book book = findBookById(bookId);
            if (returnCount > book.getBorrowedCopies()) {
                throw new BookOperationException("Return count exceeds the number of borrowed copies");
            }
            bookUtils.updateBookAvailability(book, returnCount, RETURN);
            Optional<Transaction> existingTransaction = findTransaction(borrower, book);
            if (existingTransaction.isPresent()) {
                Transaction updatedTransaction = existingTransaction.get();
                updatedTransaction.setType(TransactionType.RETURN);
                updatedTransaction.setStatus(COMPLETED);
                transactionRepository.save(updatedTransaction);
            }
            BookResponseDto response = dtoMapper.createBookResponse(book);
            logger.info("Successfully returned {} copies of book (ID: {}) by user {}", returnCount, book.getId(), borrower.getEmail());
            return new ApiResponse<>("Successfully returned " + returnCount + " copies of " + book.getTitle(), response, HTTP_OK);
        } catch (InvalidArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage());
            throw e;
        } catch (BookOperationException e) {
            logger.error("Book operation error: {}", e.getMessage());
            throw e;
        } catch (ResourceNotFoundException e) {
            logger.error("Book not found error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error returning book (ID: {}): {}", bookId, e.getMessage());
            throw new BookOperationException("Error Occurred while returning book (ID: {})" + bookId);
        }
    }
    /**
     * Purchases a book from the database.
     *
     * @param bookId the ID of the book to purchase
     * @param email the email of the user making the purchase
     * @param purchaseCount the number of copies to purchase
     * @return a wrapper containing the purchased book and a success message if the purchase is successful, or an error message if the purchase cannot be completed
     * @throws InvalidArgumentException if the purchase count is less than or equal to 0
     * @throws BookOperationException if an error occurs while purchasing the book
     * @throws ResourceNotFoundException if the book with the given ID is not found
     */
    @Override
    public ApiResponse<BookResponseDto> purchaseBook(Long bookId, String email, Integer purchaseCount)
            throws InvalidArgumentException, BookOperationException, ResourceNotFoundException {
        User purchaser = findUserByEmail(email);
        try {
            if (purchaseCount <= 0) {
                throw new InvalidArgumentException("Purchase count must be greater than 0");
            }
            Book requestedBook = findBookById(bookId);
            if (purchaseCount > requestedBook.getTotalCopies()) {
                throw new BookOperationException("Cannot purchase more copies than available copies " + requestedBook.getTotalCopies());
            }
            bookUtils.updateBookAvailability(requestedBook, purchaseCount, PURCHASE);
            Book updatedBook = findBookById(bookId);
            Transaction savePurchaseTransaction = Transaction.builder()
                    .user(purchaser)
                    .type(PURCHASE)
                    .status(COMPLETED)
                    .book(updatedBook)
                    .amount(requestedBook.getUnitPriceOfBook())
                    .build();
            transactionRepository.save(savePurchaseTransaction);
            BookResponseDto response = dtoMapper.createBookResponse(updatedBook);
            logger.info("Successfully purchased {} copies of book (ID: {}) by user {}", purchaseCount, requestedBook.getId(), purchaser.getEmail());
            return new ApiResponse<>("Successfully bought " + purchaseCount + " copies of " + requestedBook.getTitle(), response, HTTP_OK);
        } catch (InvalidArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage());
            throw e;
        } catch (BookOperationException e) {
            logger.error("Book operation error: {}", e.getMessage());
            throw e;
        } catch (ResourceNotFoundException e) {
            logger.error("Book not found error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error purchasing book (ID: {}): {}", bookId, e.getMessage());
            throw new BookOperationException("Error Occurred while purchasing book (ID: {})" + bookId);
        }
    }
    Optional<Transaction> findTransaction(User user, Book book) {
        return transactionRepository.findTransactionByUserIdAndBookIdAndType(user.getId(), book.getId(), BORROW);
    }
}
