package com.example.panttegi.board.dto;

import com.example.panttegi.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardReqDto {

    @NotBlank(message = "BAD_INPUT")
    private final String name;

    private final String color;
    private final String imageUrl;

    public Board toEntity() {
        return new Board(this.color, this.imageUrl, this.name);
    }
}
