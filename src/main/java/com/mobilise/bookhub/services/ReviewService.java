package com.mobilise.bookhub.services;

import com.mobilise.bookhub.dto.request.ReviewRequestDto;
import com.mobilise.bookhub.dto.response.ApiResponse;
import com.mobilise.bookhub.dto.response.ReviewResponseDto;
import com.mobilise.bookhub.exception.ResourceNotFoundException;
import com.mobilise.bookhub.exception.UserNotFoundException;

import java.util.List;

public interface ReviewService {
    ApiResponse<List<ReviewResponseDto>> getAllReviews();

    ApiResponse<ReviewResponseDto> getReviewById(Long id, String email);

    ApiResponse<ReviewResponseDto> createReview(ReviewRequestDto requestDto, String email, Long bookId)
            throws UserNotFoundException, ResourceNotFoundException;

    ApiResponse<ReviewResponseDto> updateReview(Long id, String email, ReviewRequestDto requestDto);

    void deleteReview(Long id, String email);
}
