package com.mobilise.bookhub.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
/**
 * Represents a response DTO for a review.
 *
 * @author codecharlan
 */
@Builder
public record ReviewResponseDto(
        Long id,
        Long userId,
        Long bookId,
        int rating,
        String comments,
        LocalDateTime reviewDate
) {
}