package com.example.panttegi.card.controller;

import com.example.panttegi.card.dto.PostCardRequestDto;
import com.example.panttegi.card.dto.CardResponseDto;
import com.example.panttegi.card.service.CardService;
import com.example.panttegi.common.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    // 카드 생성
    @PostMapping
    public ResponseEntity<CommonResDto<CardResponseDto>> postCard(
            @Valid @RequestBody PostCardRequestDto postCardRequestDto,
            @RequestHeader("Authorization") String token
    ) {

        // 나중에 토큰으로 받음
        Long userId = 1L;

        CardResponseDto card = cardService.postCard(
                postCardRequestDto.getTitle(),
                postCardRequestDto.getDescription(),
                postCardRequestDto.getPosition(),
                postCardRequestDto.getEndAt(),
                userId,
                postCardRequestDto.getManagerId(),
                postCardRequestDto.getBoardListId(),
                postCardRequestDto.getFileIds()
                );

        return new ResponseEntity<>(new CommonResDto<>("카드 생성 완료", card), HttpStatus.CREATED);
    }

    // 카드 단일 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<CommonResDto<CardResponseDto>> getCard(
            @PathVariable Long cardId
    ) {

        CardResponseDto card = cardService.getCard(cardId);

        return new ResponseEntity<>(new CommonResDto<>("카드 단일 조회 완료", card), HttpStatus.OK);
    }



}
