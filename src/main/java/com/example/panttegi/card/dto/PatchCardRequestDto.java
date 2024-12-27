package com.example.panttegi.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PatchCardRequestDto {

    private final String title;

    private final String description;

    private final int position;

    private final LocalDateTime endAt;

    private final Long managerId;

    private final Long listId;

    private final List<Long> fileIds;


}
