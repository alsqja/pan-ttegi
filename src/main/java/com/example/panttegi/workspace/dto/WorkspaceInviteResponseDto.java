package com.example.panttegi.workspace.dto;

import com.example.panttegi.common.BaseDtoDataType;
import com.example.panttegi.enums.MemberRole;
import com.example.panttegi.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkspaceInviteResponseDto implements BaseDtoDataType {
    private final MemberRole role;
    private final Long userId;
    private final Long workspaceId;

    public WorkspaceInviteResponseDto(Member member) {
        this.role = member.getRole();
        this.userId = member.getUser().getId();
        this.workspaceId = member.getWorkspace().getId();
    }
}
