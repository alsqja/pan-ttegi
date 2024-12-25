package com.example.panttegi.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginReqDto {

    private final String email;
    private final String password;
}
