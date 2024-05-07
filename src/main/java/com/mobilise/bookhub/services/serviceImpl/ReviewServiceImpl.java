package com.mobilise.bookhub.services.serviceImpl;

import com.mobilise.bookhub.dto.request.ReviewRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.ReviewResponseDto;
import com.mobilise.bookhub.entity.Book;
import com.mobilise.bookhub.entity.Review;
import com.mobilise.bookhub.entity.User;
import com.mobilise.bookhub.exception.ResourceNotFoundException;
import com.mobilise.bookhub.exception.UserNotFoundException;
import com.mobilise.bookhub.repository.BookRepository;
import com.mobilise.bookhub.repository.ReviewRepository;
import com.mobilise.bookhub.repository.UserRepository;
import com.mobilise.bookhub.services.ReviewService;
import com.mobilise.bookhub.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.*;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public ApiResponse<List<ReviewResponseDto>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponseDto> reviewResponse = reviews.stream()
                .map(DtoMapper::convertToResponseDto)
                .collect(Collectors.toList());
        return new ApiResponse<>("Reviews retrieved successfully", reviewResponse, HTTP_OK);
    }
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public ApiResponse<ReviewResponseDto> getReviewById(Long id, String email) {
        findUserByEmail(email);
        Review review = reviewRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Could not find review for id " + id));
        ReviewResponseDto reviewResponse = DtoMapper.convertToResponseDto(review);
        return new ApiResponse<>("Review retrieved successfully", reviewResponse, HTTP_OK);
    }

    @Override
    public ApiResponse<ReviewResponseDto> createReview(ReviewRequestDto requestDto, String email, Long bookId)
            throws UserNotFoundException, ResourceNotFoundException {
        Optional<User> user = Optional.ofNullable(findUserByEmail(email));
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new ResourceNotFoundException("Could not find book"));
        Review review = Review.builder()
                .user(user.orElse(null))
                .book(book)
                .rating(requestDto.rating())
                .comments(requestDto.comments())
                .build();
        Review savedReview = reviewRepository.save(review);
        ReviewResponseDto reviewResponse = DtoMapper.convertToResponseDto(savedReview);
        return new ApiResponse<>("Review created successfully", reviewResponse, HTTP_CREATED);
    }

    @Override
    public ApiResponse<ReviewResponseDto> updateReview(Long id, String email, ReviewRequestDto requestDto) {
        findUserByEmail(email);
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not available"));
        review.setRating(requestDto.rating());
        review.setComments(requestDto.comments());
        Review savedReview = reviewRepository.save(review);
        ReviewResponseDto reviewResponse = DtoMapper.convertToResponseDto(savedReview);
        return new ApiResponse<>("Review retrieved successfully", reviewResponse, HTTP_NO_CONTENT);
    }

    @Override
    public void deleteReview(Long id, String email) {
        findUserByEmail(email);
        reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not available"));
        reviewRepository.deleteById(id);
    }

}
