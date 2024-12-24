package com.example.panttegi.card.controller;

import com.example.panttegi.card.dto.PostCardRequestDto;
import com.example.panttegi.card.dto.PostCardResponseDto;
import com.example.panttegi.card.service.CardService;
import com.example.panttegi.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    // 카드 생성
    @PostMapping
    public ResponseEntity<CommonResDto<PostCardResponseDto>> postCard(
            @RequestBody PostCardRequestDto postCardRequestDto
    ) {

        PostCardResponseDto card = cardService.postCard(
                postCardRequestDto.getTitle(),
                postCardRequestDto.getDescription(),
                postCardRequestDto.getPosition(),
                postCardRequestDto.getEndAt(),
                postCardRequestDto.getManagerId(),
                postCardRequestDto.getBoardListId()
        );

        return new ResponseEntity<>(new CommonResDto<>("카드 생성 완료", card), HttpStatus.CREATED);
    }
}
