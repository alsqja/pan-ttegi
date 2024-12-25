package com.example.panttegi.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateBoardReqDto {

    @NotBlank(message = "BAD_INPUT")
    private final String name;
    
    private final String color;
    private final String imageUrl;

}
