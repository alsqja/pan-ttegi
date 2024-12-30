package com.example.panttegi.workspace.dto;

import com.example.panttegi.workspace.entity.Workspace;
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

    private final String notifyChannel;
    private final String notifyToken;

    public Workspace toEntity() {
        return new Workspace(name, description, notifyChannel, notifyToken);
    }
}
