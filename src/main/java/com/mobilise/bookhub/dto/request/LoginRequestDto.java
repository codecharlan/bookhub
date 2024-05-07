package com.mobilise.bookhub.dto.request;

import lombok.Builder;
/**
 * A DTO (Data Transfer Object) that represent a request object for logging into the system.
 *
 * @author charlancodes
 */

@Builder
public record LoginRequestDto(
        String email,
        String password
) {
}
