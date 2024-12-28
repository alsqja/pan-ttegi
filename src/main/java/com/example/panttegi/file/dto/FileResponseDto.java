package com.example.panttegi.file.dto;

import com.example.panttegi.common.BaseDtoDataType;
import com.example.panttegi.file.entity.File;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileResponseDto implements BaseDtoDataType {
    private final Long id;
    private final String url;

    public FileResponseDto(File file) {
        this.id = file.getId();
        this.url = file.getUrl();
    }
}
