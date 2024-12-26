package com.example.panttegi.user.dto;

import com.example.panttegi.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserReqDto {

    private final String name;
    private final UserRole role;
    private final String profileUrl;
}
