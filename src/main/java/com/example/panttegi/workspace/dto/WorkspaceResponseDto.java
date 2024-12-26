package com.example.panttegi.workspace.dto;

import com.example.panttegi.board.dto.BoardResDto;
import com.example.panttegi.common.BaseDtoDataType;
import com.example.panttegi.workspace.entity.Workspace;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class WorkspaceResponseDto implements BaseDtoDataType {
    private final Long id;
    private final String name;
    private final String description;
    private final List<BoardResDto> boards;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public WorkspaceResponseDto(Workspace workspace) {
        this.id = workspace.getId();
        this.name = workspace.getName();
        this.description = workspace.getDescription();
        this.boards = workspace.getBoards().stream()
                .map(BoardResDto::new)
                .collect(Collectors.toList());
        this.createdAt = workspace.getCreatedAt();
        this.updatedAt = workspace.getUpdatedAt();
    }
}
