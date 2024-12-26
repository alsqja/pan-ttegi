package com.example.panttegi.comment.dto;

import com.example.panttegi.comment.entity.Comment;
import com.example.panttegi.common.BaseDtoDataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto implements BaseDtoDataType {

    private final Long id;

    private final String content;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
    }
}
