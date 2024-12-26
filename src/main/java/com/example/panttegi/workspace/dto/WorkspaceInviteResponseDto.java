package com.example.panttegi.workspace.dto;

import com.example.panttegi.common.BaseDtoDataType;
import com.example.panttegi.enums.MemberRole;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.workspace.entity.Workspace;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkspaceInviteResponseDto implements BaseDtoDataType {
    private final MemberRole role;
    private final Long userId;
    private final Long workspaceId;

    public WorkspaceInviteResponseDto(MemberRole role, User user, Workspace workspace) {
        this.role = role;
        this.userId = user.getId();
        this.workspaceId = workspace.getId();
    }
}
