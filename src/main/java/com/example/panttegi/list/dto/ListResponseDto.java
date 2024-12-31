package com.example.panttegi.list.dto;

import com.example.panttegi.common.BaseDtoDataType;
import com.example.panttegi.list.entity.BoardList;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ListResponseDto implements BaseDtoDataType {

    private final Long id;
    private final String title;
    private final BigDecimal position;

    public ListResponseDto(BoardList boardList) {
        this.id = boardList.getId();
        this.title = boardList.getTitle();
        this.position = boardList.getPosition();
    }
}
