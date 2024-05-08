package com.mobilise.bookhub.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Represents a request to create a new review.
 *
 * @author charlancodes
 */
@Builder
public record ReviewRequestDto(
        @Min(1)
        @Max(5)
        @NotNull(message = "Book rating is required")
        int rating,

        @Size(max = 455)
        String comments
) {
}