package com.example.panttegi.workspace.controller;

import com.example.panttegi.common.CommonListResDto;
import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.workspace.dto.WorkspaceInviteRequestDto;
import com.example.panttegi.workspace.dto.WorkspaceInviteResponseDto;
import com.example.panttegi.workspace.dto.WorkspaceRequestDto;
import com.example.panttegi.workspace.dto.WorkspaceResponseDto;
import com.example.panttegi.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<CommonResDto<WorkspaceResponseDto>> createWorkspace(
            @Valid @RequestBody WorkspaceRequestDto workspaceRequestDto,
            Authentication authentication
    ) {
        WorkspaceResponseDto workspace = workspaceService.createWorkspace(
                workspaceRequestDto.getName(),
                workspaceRequestDto.getDescription(),
                authentication.getName()
        );

        return new ResponseEntity<>(new CommonResDto<>("워크스페이스 생성 완료", workspace), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonListResDto<WorkspaceResponseDto>> getAllWorkspaces(
            Authentication authentication
    ) {
        List<WorkspaceResponseDto> workspaces = workspaceService.getAllWorkspaces(authentication.getName());

        return new ResponseEntity<>(new CommonListResDto<>("워크스페이스 전체 조회 완료", workspaces), HttpStatus.OK);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<CommonResDto<WorkspaceResponseDto>> getWorkspace(
            @PathVariable Long workspaceId,
            Authentication authentication
    ) {
        WorkspaceResponseDto workspace = workspaceService.getWorkspace(workspaceId, authentication.getName());

        return new ResponseEntity<>(new CommonResDto<>("워크스페이스 단일 조회 완료", workspace), HttpStatus.OK);
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<CommonResDto<WorkspaceResponseDto>> updateWorkspace(
            @PathVariable Long workspaceId,
            @Valid @RequestBody WorkspaceRequestDto workspaceRequestDto,
            Authentication authentication
    ) {
        WorkspaceResponseDto workspace = workspaceService.updateWorkspace(
                workspaceId,
                workspaceRequestDto.getName(),
                workspaceRequestDto.getDescription(),
                authentication.getName()
        );

        return new ResponseEntity<>(new CommonResDto<>("워크스페이스 수정 완료", workspace), HttpStatus.OK);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
            @PathVariable Long workspaceId,
            Authentication authentication
    ) {
        workspaceService.deleteWorkspace(workspaceId, authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{workspaceId}/invite-member")
    public ResponseEntity<CommonResDto<WorkspaceInviteResponseDto>> inviteMember(
            @PathVariable Long workspaceId,
            @Valid @RequestBody WorkspaceInviteRequestDto workspaceInviteRequestDto,
            Authentication authentication
    ) {
        WorkspaceInviteResponseDto responseDto = workspaceService.inviteMember(
                workspaceId,
                workspaceInviteRequestDto.getEmail(),
                workspaceInviteRequestDto.getRole(),
                authentication.getName()
        );

        return new ResponseEntity<>(new CommonResDto<>("워크스페이스 초대 완료", responseDto), HttpStatus.CREATED);
    }
}
