package com.example.panttegi.board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardDetailCardDto {

    private final Long id;
    private final String title;
    private final String description;
    private final int position;
    private final LocalDateTime endAt;
    private final String managerName;
    private final Long managerId;
    private final List<BoardDetailFileDto> files;
}
