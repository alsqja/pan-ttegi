package com.example.panttegi.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkspaceRequestDto {

    @NotBlank(message = "BAD_INPUT")
    private final String name;

    @NotBlank(message = "BAD_INPUT")
    private final String description;
}
