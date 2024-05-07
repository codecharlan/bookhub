package com.mobilise.bookhub.utils;

import com.mobilise.bookhub.dto.request.BookRequestDto;
import com.mobilise.bookhub.dto.request.RegistrationRequestDto;
import com.mobilise.bookhub.dto.response.BookResponseDto;
import com.mobilise.bookhub.dto.response.LoginResponseDto;
import com.mobilise.bookhub.dto.response.RegistrationResponseDto;
import com.mobilise.bookhub.dto.response.ReviewResponseDto;
import com.mobilise.bookhub.entity.*;
import com.mobilise.bookhub.repository.AuthorRepository;
import com.mobilise.bookhub.repository.PublisherRepository;
import com.mobilise.bookhub.security.implementation.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.mobilise.bookhub.enums.BookStatus.AVAILABLE;
/**
 * A utility class for mapping DTOs to entities and vice versa.
 *
 * @author codecharlan
 */
@RequiredArgsConstructor
@Service
public class DtoMapper {
    private final PasswordEncoder passwordEncoder;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    /**
     * Creates a new User entity from the provided RegistrationRequestDto.
     *
     * @param newUser the RegistrationRequestDto containing the user's details
     * @return a new User entity with the provided details
     */
    public User createNewUser(RegistrationRequestDto newUser) {

        return User.builder()
                .fullName(newUser.fullName())
                .email(newUser.email())
                .gender(newUser.gender())
                .role(newUser.role())
                .balance(BigDecimal.valueOf(45000))
                .password(passwordEncoder.encode(newUser.password()))
                .build();
    }
    /**
     * Creates a RegistrationResponseDto from the provided User entity.
     *
     * @param savedUser the User entity to be converted to a RegistrationResponseDto
     * @return a RegistrationResponseDto containing the user's details
     */
    public RegistrationResponseDto createUserResponse(User savedUser) {
        return RegistrationResponseDto.builder()
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .gender(savedUser.getGender())
                .role(savedUser.getRole())
                .balance(savedUser.getBalance())
                .build();
    }
    /**
     * Creates a LoginResponseDto from the provided UserDetailsImpl and token.
     *
     * @param saveUser the UserDetailsImpl to be converted to a LoginResponseDto
     * @param token     the JWT token to be included in the LoginResponseDto
     * @return a LoginResponseDto containing the user's details and token
     */
    public LoginResponseDto createLoginResponse(UserDetailsImpl saveUser, String token) {
        return LoginResponseDto.builder()
                .id(saveUser.getId())
                .fullName(saveUser.getFullName())
                .email(saveUser.getEmail())
                .jwtToken(token)
                .build();
    }
    /**
     * Creates a BookResponseDto from the provided Book entity.
     *
     * @param book the Book entity to be converted to a BookResponseDto
     * @return a BookResponseDto containing the book's details
     */
    public BookResponseDto createBookResponse(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .edition(book.getEdition())
                .description(book.getDescription())
                .genre(book.getGenre())
                .status(book.getStatus())
                .publicationYear(book.getPublicationYear())
                .totalCopies(book.getTotalCopies())
                .borrowedCopies(book.getBorrowedCopies())
                .unitPriceOfBook(book.getUnitPriceOfBook())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .build();
    }
    /**
     * Creates a new Book entity from the provided BookRequestDto.
     *
     * @param newBook the BookRequestDto containing the book's details
     * @return a new Book entity with the provided details
     */
    public Book createNewBook(BookRequestDto newBook) {
        Optional<Author> existingAuthor = authorRepository.findByEmailAddress(newBook.author().getEmailAddress());
        Author author = existingAuthor.orElseGet(() -> createAuthor(newBook));
        authorRepository.save(author);
        Optional<Publisher> existingPublisher = publisherRepository.findByName(newBook.publisher().getName());
        Publisher publisher = existingPublisher.orElseGet(() -> createPublisher(newBook));
        publisherRepository.save(publisher);

        return Book.builder()
                .id(newBook.id())
                .isbn(newBook.isbn())
                .title(newBook.title())
                .edition(newBook.edition())
                .description(newBook.description())
                .genre(newBook.genre())
                .status(AVAILABLE)
                .totalCopies(newBook.totalCopies())
                .publicationYear(newBook.publicationYear())
                .borrowedCopies(0L)
                .author(author)
                .publisher(publisher)
                .unitPriceOfBook(newBook.unitPriceOfBook())
                .build();
    }
    /**
     * Creates a new Author entity from the provided BookRequestDto.
     *
     * @param newBook the BookRequestDto containing the author's details
     * @return a new Author entity with the provided details
     */
    private static Author createAuthor(BookRequestDto newBook) {
        return Author.builder()
                .name(newBook.author().getName())
                .biography(newBook.author().getBiography())
                .emailAddress(newBook.author().getEmailAddress())
                .nationality(newBook.author().getNationality())
                .build();
    }
    /**
     * Creates a new Publisher entity from the provided BookRequestDto.
     *
     * @param newBook the BookRequestDto containing the publisher's details
     * @return a new Publisher entity with the provided details
     */
    private static Publisher createPublisher(BookRequestDto newBook) {
        return Publisher.builder()
                .name(newBook.publisher().getName())
                .location(newBook.publisher().getLocation())
                .contactInformation(newBook.publisher().getContactInformation())
                .build();
    }
    /**
     * Converts a Review entity to a ReviewResponseDto.
     *
     * @param review the Review entity to be converted to a ReviewResponseDto
     * @return a ReviewResponseDto containing the review's details
     */
    public static ReviewResponseDto convertToResponseDto(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .bookId(review.getBook().getId())
                .rating(review.getRating())
                .comments(review.getComments())
                .reviewDate(review.getReviewDate())
                .build();
    }
}