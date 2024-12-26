package com.example.panttegi.board.dto;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.common.BaseDtoDataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardResDto implements BaseDtoDataType {

    private final Long id;
    private final String name;
    private final String color;
    private final String imageUrl;

    public BoardResDto(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.color = board.getColor();
        this.imageUrl = board.getImageUrl();
    }
}
