package com.example.panttegi.card.controller;

import com.example.panttegi.card.dto.CardRankingResponseDto;
import com.example.panttegi.card.dto.CardResponseDto;
import com.example.panttegi.card.dto.PatchCardRequestDto;
import com.example.panttegi.card.dto.PostCardRequestDto;
import com.example.panttegi.card.dto.SearchCardResponseDto;
import com.example.panttegi.card.service.CardService;
import com.example.panttegi.common.CommonListResDto;
import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    // 카드 생성
    @PostMapping
    public ResponseEntity<CommonResDto<CardResponseDto>> postCard(
            @Valid @RequestBody PostCardRequestDto postCardRequestDto,
            Authentication authentication
    ) {

        CardResponseDto card = cardService.postCard(
                postCardRequestDto.getTitle(),
                postCardRequestDto.getDescription(),
                postCardRequestDto.getEndAt(),
                authentication.getName(),
                postCardRequestDto.getManagerId(),
                postCardRequestDto.getListId(),
                postCardRequestDto.getFileIds()
        );

        return new ResponseEntity<>(new CommonResDto<>("카드 생성 완료", card), HttpStatus.CREATED);
    }

    @GetMapping("/ranking")
    public ResponseEntity<CommonListResDto<CardRankingResponseDto>> getCardRanking() {

        List<CardRankingResponseDto> rankings = cardService.getCardRanking();

        return new ResponseEntity<>(new CommonListResDto<>("카드 랭킹 조회 완료", rankings), HttpStatus.OK);
    }

    // 카드 단일 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<CommonResDto<CardResponseDto>> getCard(
            @PathVariable Long cardId,
            Authentication authentication
    ) {

        CardResponseDto card = cardService.getCard(cardId, authentication.getName());

        return new ResponseEntity<>(new CommonResDto<>("카드 단일 조회 완료", card), HttpStatus.OK);
    }

    // 카드 검색
    @GetMapping
    public ResponseEntity<CommonResDto<SearchCardResponseDto>> searchCard(
            @RequestParam(required = false) Long workspaceId,
            @RequestParam(required = false) Long boardId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String managerName,
            @RequestParam(defaultValue = "0") int page
    ) {

        Page<CardResponseDto> pages = cardService.searchCard(workspaceId, boardId, title, description, managerName, page);

        int totalPages = pages.getTotalPages();
        long totalElements = pages.getTotalElements();
        List<CardResponseDto> content = pages.getContent();

        SearchCardResponseDto cards = new SearchCardResponseDto(totalPages, totalElements, content);

        return new ResponseEntity<>(new CommonResDto<>("카드 검색 완료", cards), HttpStatus.OK);
    }

    // 카드 수정
    @PatchMapping("/{cardId}")
    public ResponseEntity<CommonResDto<CardResponseDto>> updateCard(
            @PathVariable Long cardId,
            @Valid @RequestBody PatchCardRequestDto patchCardRequestDto,
            Authentication authentication
    ) {

        if (patchCardRequestDto.getBeforeCardId().equals(cardId) ||
                patchCardRequestDto.getAfterCardId().equals(cardId)
        ) {
            throw new CustomException(ErrorCode.BAD_INPUT);
        }

        CardResponseDto card = cardService.updateCard(
                cardId,
                patchCardRequestDto.getTitle(),
                patchCardRequestDto.getDescription(),
                patchCardRequestDto.getBeforeCardId(),
                patchCardRequestDto.getAfterCardId(),
                patchCardRequestDto.getEndAt(),
                authentication.getName(),
                patchCardRequestDto.getManagerId(),
                patchCardRequestDto.getListId(),
                patchCardRequestDto.getFileIds()
        );

        return new ResponseEntity<>(new CommonResDto<>("카드 수정 완료", card), HttpStatus.OK);
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable Long cardId,
            Authentication authentication
    ) {

        cardService.deleteCard(cardId, authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
