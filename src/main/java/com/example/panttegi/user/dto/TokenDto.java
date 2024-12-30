package com.example.panttegi.user.dto;

import com.example.panttegi.common.BaseDtoDataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenDto implements BaseDtoDataType {

    private final String accessToken;
    private final String refreshToken;
}
