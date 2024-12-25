package com.example.panttegi.board.model;

import com.example.panttegi.board.dto.BoardReqDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardForUpdate {

    private final String name;
    private final String color;
    private final String imageUrl;
    private final Long boardId;
    private final Long workspaceId;
    private final String email;

    public BoardForUpdate(BoardReqDto dto, Long boardId, Long workspaceId, String email) {
        this.name = dto.getName();
        this.color = dto.getColor();
        this.imageUrl = dto.getImageUrl();
        this.boardId = boardId;
        this.workspaceId = workspaceId;
        this.email = email;
    }
}
