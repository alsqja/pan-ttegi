package com.example.panttegi.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PatchCardRequestDto {

    private String title;

    private String description;

    private Long beforeCardId;

    private Long afterCardId;

    private LocalDateTime endAt;

    private Long managerId;

    private Long listId;

    private List<Long> fileIds;


}
