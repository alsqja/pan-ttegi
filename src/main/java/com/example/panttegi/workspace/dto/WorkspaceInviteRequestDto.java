package com.example.panttegi.workspace.dto;

import com.example.panttegi.enums.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkspaceInviteRequestDto {

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message = "BAD_INPUT")
    private final String email;

    @NotNull(message = "BAD_INPUT")
    private final MemberRole role;
}
