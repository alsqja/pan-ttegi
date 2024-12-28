package com.example.panttegi.list.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateListRequestDto {

    @NotNull(message = "BAD_INPUT")
    private final Long boardId;

    @NotBlank(message = "BAD_INPUT")
    private final String title;
}
