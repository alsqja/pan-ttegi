package com.example.panttegi.card.dto;

import com.example.panttegi.card.entity.Card;
import com.example.panttegi.common.BaseDtoDataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PostCardResponseDto implements BaseDtoDataType {

    private final Long id;
    private final String title;
    private final String description;
    private final int position;
    private final LocalDateTime endAt;
    private final Long userId;
    private final Long managerId;
    private final Long BoardListId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

//    private List<File> files = new ArrayList<>();

    public PostCardResponseDto(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.position = card.getPosition();
        this.endAt = card.getEndAt();
        this.userId = card.getUser().getId();
        this.managerId = card.getManager().getId();
        this.BoardListId = card.getBoardList().getId();
        this.createdAt = card.getCreatedAt();
        this.updatedAt = card.getUpdatedAt();
    }



}
