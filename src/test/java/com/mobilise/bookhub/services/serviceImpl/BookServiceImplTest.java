package com.mobilise.bookhub.services.serviceImpl;

import com.mobilise.bookhub.dto.request.BookRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.BookResponseDto;
import com.mobilise.bookhub.entity.*;
import com.mobilise.bookhub.enums.BookStatus;
import com.mobilise.bookhub.enums.Genre;
import com.mobilise.bookhub.enums.TransactionType;
import com.mobilise.bookhub.exception.*;
import com.mobilise.bookhub.repository.*;
import com.mobilise.bookhub.utils.BookUtils;
import com.mobilise.bookhub.utils.DtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private PublisherRepository publisherRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private DtoMapper dtoMapper;
    @Mock
    private BookUtils bookUtils;
    @InjectMocks
    private BookServiceImpl bookService;
    BookRequestDto bookRequestDto;
    BookResponseDto bookResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookRequestDto = BookRequestDto.builder().isbn("978-1473622341").title("The Intruder").edition("1st").description("A gripping story of a family and the choices they have to make").genre(Genre.FICTION).status(BookStatus.AVAILABLE).totalCopies(10L).borrowedCopies(0L).publicationYear(2023).unitPriceOfBook(BigDecimal.valueOf(1500)).author(Author.builder().id(1L).name("Femi Fadugba").nationality("America").biography("Creative").build()).publisher(Publisher.builder().id(1L).name("Penguin Random House").contactInformation("penguin@rans.com").build()).build();

        bookResponseDto = BookResponseDto.builder().isbn("978-1473622341").title("The Intruder").edition("1st").description("A gripping story of a family and the choices they have to make").genre(Genre.FICTION).status(BookStatus.AVAILABLE).totalCopies(10L).borrowedCopies(0L).publicationYear(2023).unitPriceOfBook(BigDecimal.valueOf(1500)).author(Author.builder().id(1L).name("Femi Fadugba").nationality("America").biography("Creative").build()).publisher(Publisher.builder().id(1L).name("Penguin Random House").contactInformation("penguin@rans.com").build()).build();
    }

    @Test
    void testCreateBook_Success() {
        User user = new User();
        Book book = new Book();

        ApiResponse<BookResponseDto> expectedResponse = new ApiResponse<>("Book created successfully (ID: null)", bookResponseDto, HTTP_CREATED);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(dtoMapper.createNewBook(any(BookRequestDto.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(dtoMapper.createBookResponse(any(Book.class))).thenReturn(bookResponseDto);

        ApiResponse<BookResponseDto> actualResponse = bookService.createBook("test@example.com", bookRequestDto);

        // Assertions
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testEditBook_Success() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Original Title");
        existingBook.setIsbn("Original ISBN");
        existingBook.setAuthor(Author.builder().name("Simo").nationality("Nigeria").emailAddress("dea@gamil.com").build());
        existingBook.setPublisher(Publisher.builder().id(2L).name("Manning Corp").location("New York").build());

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(existingBook));
        when(authorRepository.findByEmailAddress(anyString())).thenReturn(Optional.of(Author.builder().name("Simo").nationality("Nigeria").emailAddress("dea@gamil.com").build()));
        when(publisherRepository.findByName(anyString())).thenReturn(Optional.of(new Publisher()));
        when(dtoMapper.createBookResponse(any(Book.class))).thenReturn(bookResponseDto);
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApiResponse<BookResponseDto> response = bookService.editBook("test@example.com", 1L, bookRequestDto);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT.value(), response.status());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testGetAllBooks_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        PageRequest pageable = PageRequest.of(0, 10);
        List<Book> books = Collections.singletonList(new Book());
        Page<Book> bookPage = new PageImpl<>(books);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(bookPage);
        when(dtoMapper.createBookResponse(any(Book.class))).thenReturn(bookResponseDto);

        ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>> response = bookService.getAllBooks("test@example.com", 0, 10, "title", "asc", null);

        // Assertions
        assertEquals(HttpStatus.OK.value(), response.status());
        verify(bookRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetBookById_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(dtoMapper.createBookResponse(any(Book.class))).thenReturn(bookResponseDto);

        ApiResponse<BookResponseDto> response = bookService.getBookById("test@example.com", 1L);

        // Assertions
        assertEquals(HttpStatus.OK.value(), response.status());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetBookById_BookNotFound() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById("test@example.com", 1L);
        });

        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testDeleteBook_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);
        book.setBorrowedCopies(0L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        ApiResponse<String> response = bookService.deleteBook("test@example.com", 1L);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT.value(), response.status());
        verify(bookRepository, times(1)).delete(any(Book.class));
    }

    @Test
    void testDeleteBook_BookCannotBeDeleted() {
        User user = new User();
        user.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);
        book.setBorrowedCopies(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        assertThrows(BookOperationException.class, () -> {
            bookService.deleteBook("test@example.com", 1L);
        });
        verify(bookRepository, times(0)).delete(any(Book.class));
    }

    @Test
    void testDeleteBook_BookNotFound() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookOperationException.class, () -> {
            bookService.deleteBook("test@example.com", 1L);
        });
        verify(bookRepository, times(0)).delete(any(Book.class));
    }

    @Test
    void testSearchBooksByTitleOrAuthor_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        PageRequest pageable = PageRequest.of(0, 10);
        List<Book> books = Collections.singletonList(new Book());
        Page<Book> bookPage = new PageImpl<>(books);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(anyString(), anyString(), any(PageRequest.class))).thenReturn(bookPage);
        when(dtoMapper.createBookResponse(any(Book.class))).thenReturn(bookResponseDto);

        ApiResponse<ApiResponse.Wrapper<List<BookResponseDto>>> response = bookService.searchBooksByTitleOrAuthor("test@example.com", 0, 10, "searchTerm");

        // Assertions
        assertEquals(HttpStatus.OK.value(), response.status());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(anyString(), anyString(), any(PageRequest.class));
    }

    @Test
    void testSearchBooksByTitleOrAuthor_EmptySearchTerm() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(BookOperationException.class, () -> {
            bookService.searchBooksByTitleOrAuthor("test@example.com", 0, 10, "");
        });
        verify(bookRepository, times(0)).findByTitleContainingIgnoreCaseOrAuthor_NameContainingIgnoreCase(anyString(), anyString(), any(PageRequest.class));
    }

    @Test
    void testBorrowBook_Success() {
        User borrower = new User();
        borrower.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);
        book.setTotalCopies(5L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(dtoMapper.createBookResponse(any(Book.class))).thenReturn(bookResponseDto);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        ApiResponse<BookResponseDto> response = bookService.borrowBook(1L, "test@example.com", 3);

        // Assertions
        assertEquals(HttpStatus.OK.value(), response.status());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testBorrowBook_InvalidArgument() {
        User borrower = new User();
        borrower.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        assertThrows(InvalidArgumentException.class, () -> {
            bookService.borrowBook(1L, "test@example.com", -3);
        });

        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void testBorrowBook_BookNotFound() {
        User borrower = new User();
        borrower.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.borrowBook(1L, "test@example.com", 3);
        });

        verify(transactionRepository, times(0)).save(any(Transaction.class));

    }

    @Test
    void testReturnBook_Success() {
        User borrower = new User();
        borrower.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);
        book.setBorrowedCopies(3L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(transactionRepository.findTransactionByUserIdAndBookIdAndType(anyLong(), anyLong(), any(TransactionType.class))).thenReturn(Optional.of(new Transaction()));
        when(dtoMapper.createBookResponse(any(Book.class))).thenReturn(bookResponseDto);

        ApiResponse<BookResponseDto> response = bookService.returnBook("test@example.com", 1L, 2);

        // Assertions
        assertEquals(HttpStatus.OK.value(), response.status());
    }

    @Test
    void testReturnBook_InvalidArgument() {
        User borrower = new User();
        borrower.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertThrows(InvalidArgumentException.class, () -> {
            bookService.returnBook("test@example.com", 1L, -2);
        });
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void testReturnBook_BookNotFound() {
        User borrower = new User();
        borrower.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.returnBook("test@example.com", 1L, 2);
        });
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void testPurchaseBook_Success() {
        User purchaser = new User();
        purchaser.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);
        book.setTotalCopies(5L);
        book.setUnitPriceOfBook(BigDecimal.valueOf(10));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(purchaser));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(dtoMapper.createBookResponse(any(Book.class))).thenReturn(bookResponseDto);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        ApiResponse<BookResponseDto> response = bookService.purchaseBook(1L, "test@example.com", 3);

        // Assertions
        assertEquals(HttpStatus.OK.value(), response.status());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testPurchaseBook_InvalidArgument() {
        User purchaser = new User();
        purchaser.setEmail("test@example.com");
        User borrower = new User();
        borrower.setEmail("test@example.com");
        Book book = new Book();
        book.setId(1L);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertThrows(InvalidArgumentException.class, () -> {
            bookService.purchaseBook(1L, "test@example.com", -3);
        });
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void testPurchaseBook_BookNotFound() {
        User purchaser = new User();
        purchaser.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(purchaser));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.purchaseBook(1L, "test@example.com", 3);
        });
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void testFindBookById_Success() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Book foundBook = bookService.findBookById(1L);

        // Assertions
        assertNotNull(foundBook);
        assertEquals(1L, foundBook.getId());
    }

    @Test
    void testFindBookById_NotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.findBookById(1L);
        });
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindTransaction_Success() {
        User user = new User();
        user.setId(1L);
        Book book = new Book();
        book.setId(1L);

        when(transactionRepository.findTransactionByUserIdAndBookIdAndType(anyLong(), anyLong(), any(TransactionType.class))).thenReturn(Optional.of(new Transaction()));

        Optional<Transaction> transaction = bookService.findTransaction(user, book, TransactionType.BORROW);

        // Assertions
        Assertions.assertTrue(transaction.isPresent());
    }

    @Test
    void testFindTransaction_NotFound() {
        User user = new User();
        user.setId(1L);
        Book book = new Book();
        book.setId(1L);
        when(transactionRepository.findTransactionByUserIdAndBookIdAndType(anyLong(), anyLong(), any(TransactionType.class))).thenReturn(Optional.empty());

        Optional<Transaction> transaction = bookService.findTransaction(user, book, TransactionType.BORROW);

        // Assertions
        Assertions.assertFalse(transaction.isPresent());
    }

    @Test
    void testCheckForDuplicateBook_DuplicateFound() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Title");
        existingBook.setAuthor(Author.builder().name("Simo").nationality("Nigeria").emailAddress("dea@gamil.com").build());
        when(bookRepository.findByTitleAndAuthor_Name(anyString(), anyString())).thenReturn(Optional.of(existingBook));

        assertThrows(BookAlreadyCreatedException.class, () -> {
            bookService.checkForDuplicateBook("Title", "Simo");
        });

        verify(bookRepository, times(1)).findByTitleAndAuthor_Name(anyString(), anyString());
    }

    @Test
    void testCheckForDuplicateBook_NoDuplicate() {
        when(bookRepository.findByTitleAndAuthor_Name(anyString(), anyString())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            bookService.checkForDuplicateBook("Title", "Author");
        });

        verify(bookRepository, times(1)).findByTitleAndAuthor_Name(anyString(), anyString());
    }

    @Test
    void testFindUserByEmail_Success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User foundUser = bookService.findUserByEmail("test@example.com");

        // Assertions
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void testFindUserByEmail_NotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            bookService.findUserByEmail("test@example.com");
        });
        verify(userRepository, times(1)).findByEmail(anyString());
    }
}
