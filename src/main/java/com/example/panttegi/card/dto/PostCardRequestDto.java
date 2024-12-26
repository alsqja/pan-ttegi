package com.example.panttegi.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
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

    private final Long managerId;

    @NotNull(message = "BAD_INPUT")
    private final Long listId;

    private final List<Long> fileIds;


}
