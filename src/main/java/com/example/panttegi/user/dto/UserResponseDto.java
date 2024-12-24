package com.example.panttegi.user.dto;

import com.example.panttegi.common.BaseDtoDataType;
import com.example.panttegi.enums.UserRole;
import com.example.panttegi.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserResponseDto implements BaseDtoDataType {

    private final Long id;
    private final String email;
    private final String name;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
