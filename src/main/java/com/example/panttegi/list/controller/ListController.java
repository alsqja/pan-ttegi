package com.example.panttegi.list.controller;

import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.list.dto.CreateListRequestDto;
import com.example.panttegi.list.dto.ListResponseDto;
import com.example.panttegi.list.dto.UpdateListRequestDto;
import com.example.panttegi.list.service.ListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/lists")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @PostMapping
    public ResponseEntity<CommonResDto<ListResponseDto>> createList(
            @PathVariable Long workspaceId,
            @Valid @RequestBody CreateListRequestDto createListRequestDto,
            Authentication authentication
    ) {
        ListResponseDto response = listService.createList(
                workspaceId,
                createListRequestDto.getBoardId(),
                createListRequestDto.getTitle(),
                authentication.getName()
        );

        return new ResponseEntity<>(new CommonResDto<>("리스트 생성 완료", response), HttpStatus.CREATED);
    }

    @PatchMapping("/{listId}")
    public ResponseEntity<CommonResDto<ListResponseDto>> updateList(
            @PathVariable Long workspaceId,
            @PathVariable Long listId,
            @Valid @RequestBody UpdateListRequestDto updateListRequestDto,
            Authentication authentication
    ) {
        ListResponseDto response = listService.updateList(
                workspaceId,
                listId,
                updateListRequestDto.getTitle(),
                updateListRequestDto.getTargetIndex(),
                authentication.getName()
        );

        return new ResponseEntity<>(new CommonResDto<>("리스트 수정 완료", response), HttpStatus.OK);
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(
            @PathVariable Long workspaceId,
            @PathVariable Long listId,
            Authentication authentication
    ) {
        listService.deleteList(workspaceId, listId, authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
