package com.example.panttegi.workspace.controller;

import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.workspace.dto.WorkspaceRequestDto;
import com.example.panttegi.workspace.dto.WorkspaceResponseDto;
import com.example.panttegi.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<CommonResDto<WorkspaceResponseDto>> createWorkspace(
            @Valid @RequestBody WorkspaceRequestDto requestDto,
            @RequestHeader("Authorization") String token
            ) {
        Long userId = 1L;
        WorkspaceResponseDto workspace = workspaceService.createWorkspace(requestDto, userId);

        return new ResponseEntity<>(new CommonResDto<>("워크스페이스 생성 완료", workspace), HttpStatus.CREATED);
    }
}
