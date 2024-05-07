package com.mobilise.bookhub.services.serviceImpl;

import com.mobilise.bookhub.dto.request.ReviewRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.ReviewResponseDto;
import com.mobilise.bookhub.entity.Book;
import com.mobilise.bookhub.entity.Review;
import com.mobilise.bookhub.entity.User;
import com.mobilise.bookhub.enums.Gender;
import com.mobilise.bookhub.enums.Role;
import com.mobilise.bookhub.exception.ResourceNotFoundException;
import com.mobilise.bookhub.repository.BookRepository;
import com.mobilise.bookhub.repository.ReviewRepository;
import com.mobilise.bookhub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private ReviewServiceImpl reviewService;
    ReviewResponseDto reviewResponseDto;
    Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewResponseDto = ReviewResponseDto.builder().id(1L).userId(1L).bookId(1L).rating(5).comments("Excellent book!").reviewDate(LocalDateTime.now()).build();
        review = new Review();
        review.setId(1L);
        review.setRating(4);
        review.setComments("Excellent book!");
        review.setReviewDate(LocalDateTime.now());
        review.setUser(User.builder().id(1L).fullName("John Doe").email("ernest@charlancodes.org").password("Wer456gghL@#").role(Role.USER).gender(Gender.MALE).balance(BigDecimal.ZERO).build());
        review.setBook(new Book());
    }

    @Test
    void testGetAllReviews_Success() {
        List<Review> reviews = Collections.singletonList(review);
        when(reviewRepository.findAll()).thenReturn(reviews);

        ApiResponse<List<ReviewResponseDto>> response = reviewService.getAllReviews();

        // Assertions
        assertNotNull(response);
        assertEquals("Reviews retrieved successfully", response.message());
        assertEquals(200, response.status());
        assertFalse(response.data().isEmpty());
    }

    @Test
    void testGetReviewById_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new Book()));

        ApiResponse<ReviewResponseDto> response = reviewService.getReviewById(1L, "ernest@charlancodes.org");

        // Assertions
        assertNotNull(response);
        assertEquals("Review retrieved successfully", response.message());
        assertEquals(200, response.status());
        assertNotNull(response.data());
    }

    @Test
    void testCreateReview_Success() {
        ReviewRequestDto requestDto = new ReviewRequestDto(5, "Great book!");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(new Book()));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ApiResponse<ReviewResponseDto> response = reviewService.createReview(requestDto, "ernest@charlancodes.org", 1L);

        // Assertions
        assertNotNull(response);
        assertEquals("Review created successfully", response.message());
        assertEquals(201, response.status());
        assertNotNull(response.data());
    }

    @Test
    void testUpdateReview_Success() {
        ReviewRequestDto requestDto = new ReviewRequestDto(4, "Enjoyed the book");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ApiResponse<ReviewResponseDto> response = reviewService.updateReview(1L, "ernest@charlancodes.org", requestDto);

        // Assertions
        assertNotNull(response);
        assertEquals("Review retrieved successfully", response.message());
        assertEquals(204, response.status());
        assertNotNull(response.data());
    }

    @Test
    void testDeleteReview_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(new Review()));
        //Assertions
        assertDoesNotThrow(() -> reviewService.deleteReview(1L, "ernest@charlancodes.org"));
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteReview_ThrowsResourceNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview(1L, "ernest@charlancodes.org"));
        verify(reviewRepository, never()).deleteById(anyLong());
    }

}
