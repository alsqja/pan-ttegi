package com.example.panttegi.board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardDetailListDto {

    private final Long id;
    private final String title;
    private final BigDecimal position;
    private final List<BoardDetailCardDto> cards;
}
