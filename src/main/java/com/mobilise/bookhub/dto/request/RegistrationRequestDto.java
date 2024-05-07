package com.mobilise.bookhub.dto.request;

import com.mobilise.bookhub.enums.Gender;
import com.mobilise.bookhub.enums.Role;
import lombok.Builder;

import java.io.Serializable;
/**
 * Represents a DTO for registration request.
 *
 * @author charlancodes
 */
@Builder
public record RegistrationRequestDto(
        String fullName,
        String email,
        String password,
        Gender gender,
        Role role
) implements Serializable {
}
