package com.mobilise.bookhub.dto.response;

import com.mobilise.bookhub.enums.Gender;
import com.mobilise.bookhub.enums.Role;
import lombok.Builder;

import java.math.BigDecimal;
/**
 * Represents the response data for a successful user registration.
 *
 * @author codecharlan
 */
@Builder
public record RegistrationResponseDto(
        String fullName,
        String email,
        Gender gender,
        Role role,
        BigDecimal balance
) {
}
