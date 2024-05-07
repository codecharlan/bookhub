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
@RequiredArgsConstructor
@Service
public class DtoMapper {
    private final PasswordEncoder passwordEncoder;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
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

    public RegistrationResponseDto createUserResponse(User savedUser) {
        return RegistrationResponseDto.builder()
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .gender(savedUser.getGender())
                .role(savedUser.getRole())
                .balance(savedUser.getBalance())
                .build();
    }

    public LoginResponseDto createLoginResponse(UserDetailsImpl saveUser, String token) {
        return LoginResponseDto.builder()
                .id(saveUser.getId())
                .fullName(saveUser.getFullName())
                .email(saveUser.getEmail())
                .jwtToken(token)
                .build();
    }
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
    private static Author createAuthor(BookRequestDto newBook) {
        return Author.builder()
                .name(newBook.author().getName())
                .biography(newBook.author().getBiography())
                .emailAddress(newBook.author().getEmailAddress())
                .nationality(newBook.author().getNationality())
                .build();
    }

    private static Publisher createPublisher(BookRequestDto newBook) {
        return Publisher.builder()
                .name(newBook.publisher().getName())
                .location(newBook.publisher().getLocation())
                .contactInformation(newBook.publisher().getContactInformation())
                .build();
    }

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