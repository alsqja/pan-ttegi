package com.example.panttegi.card.dto;

import com.example.panttegi.common.BaseDtoDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardRankingResponseDto implements BaseDtoDataType {

    private final CardResponseDto card;
    private final double viewCount;
    private final Long rank;
}
