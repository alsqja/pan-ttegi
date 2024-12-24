package com.example.panttegi.user.dto;

import com.example.panttegi.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupRequestDto {

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "BAD_INPUT")
    private final String email;

    @NotBlank(message = "BAD_INPUT")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "INVALID_PASSWORD"
    )
    private final String password;

    @NotBlank(message = "BAD_INPUT")
    private final String name;

    private final String profileUrl;

    private final UserRole role;
}
