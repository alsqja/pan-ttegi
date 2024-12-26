package com.example.panttegi.board.dto;

import com.example.panttegi.common.BaseDtoDataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardDetailResDto implements BaseDtoDataType {

    private final Long id;
    private final String name;
    private final String color;
    private final String imageUrl;
    private final List<BoardDetailListDto> lists;
}
