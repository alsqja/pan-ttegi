package com.example.panttegi.board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardDetailListDto {

    private final Long id;
    private final String title;
    private final int position;
    private final List<BoardDetailCardDto> cards;
}
