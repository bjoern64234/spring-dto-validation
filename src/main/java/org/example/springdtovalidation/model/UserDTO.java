package org.example.springdtovalidation.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record UserDTO(
        @NotBlank(message = "The username can not be blank.")
        String username,
        @Min(value = 16, message = "You must be oder than 15")
        int age,
        @Email(message = "The email must be valid.")
        String email,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).+$", message="The password must be valid.")
        String password) {
}
