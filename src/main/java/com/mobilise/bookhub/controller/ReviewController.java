package com.mobilise.bookhub.controller;

import com.mobilise.bookhub.dto.request.ReviewRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.ReviewResponseDto;
import com.mobilise.bookhub.exception.ResourceNotFoundException;
import com.mobilise.bookhub.exception.UserNotFoundException;
import com.mobilise.bookhub.security.JwtService;
import com.mobilise.bookhub.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.mobilise.bookhub.constants.Constants.*;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

/**
 * * mobilise-book-hub
 * * BookController.java
 * * May 05, 2024,
 * Controller for managing reviews.
 *
 * @author charlancodes
 */
@RestController
@RequestMapping(BASE_REVIEW_API_URL)
@RequiredArgsConstructor
public class ReviewController {
    /**
     * Service for managing reviews.
     */
    private final ReviewService reviewService;
    private final JwtService jwtService;

    /**
     * Retrieves all reviews.
     *
     * @return A ResponseEntity containing an ApiResponse with a list of ReviewResponseDto objects.
     */
    @GetMapping(GET_ALL_URL)
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getAllReviews() {
        ApiResponse<List<ReviewResponseDto>> response = reviewService.getAllReviews();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param id The ID of the review to retrieve.
     * @return A ResponseEntity containing an ApiResponse with a single ReviewResponseDto object.
     */

    @GetMapping(ID_URL)
    public ResponseEntity<ApiResponse<ReviewResponseDto>> getReviewById(@PathVariable Long id, String email) {
        ApiResponse<ReviewResponseDto> response = reviewService.getReviewById(id, email);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

    /**
     * Creates a new review.
     *
     * @param requestDto          The data for the new review.
     * @param authorizationHeader The bearer token of the user creating the review.
     * @param bookId              The ID of the book being reviewed.
     * @return A ResponseEntity containing an ApiResponse with a single ReviewResponseDto object.
     * @throws UserNotFoundException     If the user with the given email does not exist.
     * @throws ResourceNotFoundException If the book with the given ID does not exist.
     */
    @PostMapping(CREATE_URL)
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(@RequestBody ReviewRequestDto requestDto,
                                                                       @RequestHeader(name = AUTHORIZATION_HEADER) String authorizationHeader,
                                                                       @RequestParam Long bookId) {
        try {
            Map<String, String> userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
            String email = userDetails.get("email");
            ApiResponse<ReviewResponseDto> response = reviewService.createReview(requestDto, email, bookId);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
        } catch (UserNotFoundException | ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>("Failed to create review: " + e.getMessage(), null, HTTP_NOT_FOUND), HttpStatusCode.valueOf(404));
        }
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id                  The ID of the review to delete.
     * @param authorizationHeader The bearer token of the user creating the review.
     * @param requestDto          The data for the new review.
     * @return A ResponseEntity with no content.
     */
    @PutMapping(EDIT_URL)
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(@PathVariable Long id,
                                                                       @RequestHeader(name = AUTHORIZATION_HEADER) String authorizationHeader,
                                                                       @RequestBody ReviewRequestDto requestDto) {
        Map<String, String> userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
        String email = userDetails.get("email");
        ApiResponse<ReviewResponseDto> response = reviewService.updateReview(id, email, requestDto);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id                  The ID of the review to delete.
     * @param authorizationHeader The bearer token of the user creating the review.
     * @return A ResponseEntity with no content.
     */
    @DeleteMapping(DELETE_URL)
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @RequestHeader(name = AUTHORIZATION_HEADER) String authorizationHeader) {
        Map<String, String> userDetails = jwtService.parseTokenClaims(authorizationHeader.substring(7));
        String email = userDetails.get("email");
        reviewService.deleteReview(id, email);
        return ResponseEntity.noContent().build();
    }
}
