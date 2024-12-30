package com.example.panttegi.user.dto;

import com.example.panttegi.common.BaseDtoDataType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResDto implements BaseDtoDataType {

    private final String tokenAuthScheme;
    private final String accessToken;
}
