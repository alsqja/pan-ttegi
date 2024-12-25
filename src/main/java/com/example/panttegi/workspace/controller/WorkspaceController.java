package com.example.panttegi.workspace.controller;

import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.workspace.dto.WorkspaceRequestDto;
import com.example.panttegi.workspace.dto.WorkspaceResponseDto;
import com.example.panttegi.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
