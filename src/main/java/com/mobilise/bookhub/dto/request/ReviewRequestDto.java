package com.mobilise.bookhub.dto.request;
import lombok.Builder;
/**
 * Represents a request to create a new review.
 * @author charlancodes
 */
@Builder
public record ReviewRequestDto(
        int rating,
        String comments
) {
}