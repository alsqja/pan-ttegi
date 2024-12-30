package com.example.panttegi.card.dto;

import com.example.panttegi.file.entity.File;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileDto {

    private final Long id;

    private final String url;

    public FileDto(File file) {
        this.id = file.getId();
        this.url = file.getUrl();
    }
}
