package com.example.panttegi.workspace.dto;

import com.example.panttegi.common.BaseDtoDataType;
import com.example.panttegi.workspace.entity.Workspace;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class WorkspaceResponseDto implements BaseDtoDataType {
    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public WorkspaceResponseDto(Workspace workspace){
        this.id = workspace.getId();
        this.name = workspace.getName();
        this.description = workspace.getDescription();
        this.createdAt = workspace.getCreatedAt();
        this.updatedAt = workspace.getUpdatedAt();
    }
}
