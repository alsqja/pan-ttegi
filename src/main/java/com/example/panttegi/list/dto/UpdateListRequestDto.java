package com.example.panttegi.list.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateListRequestDto {

    private final String title;
    private final int targetIndex;
}
