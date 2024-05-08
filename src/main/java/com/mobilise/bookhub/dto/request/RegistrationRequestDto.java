package com.mobilise.bookhub.dto.request;

import com.mobilise.bookhub.enums.Gender;
import com.mobilise.bookhub.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;
/**
 * Represents a DTO for registration request.
 *
 * @author charlancodes
 */
@Builder
public record RegistrationRequestDto(
        @NotNull(message = "Fullname is required")
        String fullName,
        @Email(message = "Must match a proper email")
        @NotNull(message = "Email is required")
        String email,
        @Size(message = "Password must not be less than 8", min = 6)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,20}$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one number")
        String password,
        @NotNull
        Gender gender,
        @NotNull
        Role role
) implements Serializable {
}
