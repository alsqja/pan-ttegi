package com.example.panttegi.card.dto;

import com.example.panttegi.file.File;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostCardRequestDto {

    @NotBlank(message = "BAD_INPUT")
    private final String title;

    @NotBlank(message = "BAD_INPUT")
    private final String description;

    @NotNull(message = "BAD_INPUT")
    private final int position;

    private final LocalDateTime endAt;

    @NotNull(message = "BAD_INPUT")
    private final Long user;

    private final Long managerId;

    @NotNull(message = "BAD_INPUT")
    private final Long boardListId;

}
