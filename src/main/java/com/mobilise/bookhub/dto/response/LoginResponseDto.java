package com.mobilise.bookhub.dto.response;

import lombok.Builder;
/**
 * Represents the response data for a successful login operation.
 *
 * @author codecharlan
 */
@Builder
public record LoginResponseDto(
        Long id,
        String fullName,
        String email,
        String jwtToken) {
}
