package com.example.panttegi.common;

import java.util.List;

public class CommonListResDto<T extends BaseDtoDataType> {

    private final String message;
    private final List<T> data;

    public CommonListResDto(String message, List<T> data) {
        this.message = message;
        this.data = data;
    }
}
