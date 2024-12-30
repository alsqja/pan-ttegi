package com.example.panttegi.user.dto;

import com.example.panttegi.enums.UserRole;
import com.example.panttegi.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("profile_url")
    private final String profileUrl;

    private final UserRole role;

    public User toEntity() {
        return new User(this.email, this.password, this.profileUrl, this.name, this.role);
    }
}
