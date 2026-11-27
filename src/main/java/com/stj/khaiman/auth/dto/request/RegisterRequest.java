package com.stj.khaiman.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username must not be blank")
        @Size(max = 100, message = "Username must not exceed 100 characters")
        String username,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 8, max = 72, message = "Password must be 8-72 characters")
        String password,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Wrong email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email
) {
}
