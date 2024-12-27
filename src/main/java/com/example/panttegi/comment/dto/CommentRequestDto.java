package com.example.panttegi.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CommentRequestDto {

    @NotNull
    private final String content;

    @JsonCreator
    public CommentRequestDto(String content) {
        this.content = content;
    }

}
