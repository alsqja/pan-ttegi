package com.example.panttegi.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshReqDto {

    @NotBlank(message = "BAD_INPUT")
    private final String refreshToken;

    @JsonCreator
    public RefreshReqDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
