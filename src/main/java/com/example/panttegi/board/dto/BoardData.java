package com.example.panttegi.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardData {

    private Long boardId;
    private String boardName;
    private String boardColor;
    private String boardImageUrl;

    private Long listId;
    private String listTitle;
    private int listPosition;

    private Long cardId;
    private String cardTitle;
    private String cardDescription;
    private int cardPosition;
    private LocalDateTime cardEndAt;
    private String managerName;
    private Long managerId;

    private Long fileId;
    private String fileUrl;
}
