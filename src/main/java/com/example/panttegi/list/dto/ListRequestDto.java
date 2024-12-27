package com.example.panttegi.list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ListRequestDto {

    @NotBlank(message = "BAD_INPUT")
    private final String title;

    @NotNull(message = "BAD_INPUT")
    private final Integer targetIndex;
}
