package com.example.panttegi.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "BAD_INPUT")
    private final String content;

}
