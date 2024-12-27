package com.example.panttegi.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UpdateCommentRequestDto {

    @NotBlank(message = "BAD_INPUT")
    private final String content;

    @JsonCreator
    public UpdateCommentRequestDto(String content) {
        this.content = content;
    }

}
