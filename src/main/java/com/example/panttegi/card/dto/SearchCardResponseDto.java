package com.example.panttegi.card.dto;

import com.example.panttegi.common.BaseDtoDataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchCardResponseDto implements BaseDtoDataType {

    private final int totalPages;

    private final Long totalElements;

    private List<CardResponseDto> cards;

    public SearchCardResponseDto(int totalPages, Long totalElements, List<CardResponseDto> cards) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.cards = cards;
    }
}
