package com.example.panttegi.list.controller;

import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.list.dto.ListRequestDto;
import com.example.panttegi.list.dto.ListResponseDto;
import com.example.panttegi.list.service.ListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/{boardId}/lists")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @PostMapping
    public ResponseEntity<CommonResDto<ListResponseDto>> createList(
            @PathVariable Long boardId,
            @Valid @RequestBody ListRequestDto listRequestDto,
            Authentication authentication
    ) {
        ListResponseDto response = listService.createList(
                boardId,
                listRequestDto.getTitle(),
                authentication.getName()
        );

        return new ResponseEntity<>(new CommonResDto<>("리스트 생성 완료", response), HttpStatus.CREATED);
    }

    @PatchMapping("/{listId}")
    public ResponseEntity<CommonResDto<ListResponseDto>> updateList(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @Valid @RequestBody ListRequestDto listRequestDto,
            Authentication authentication
    ) {
        ListResponseDto response = listService.updateList(
                listId,
                listRequestDto.getTitle(),
                listRequestDto.getTargetIndex(),
                authentication.getName());

        return new ResponseEntity<>(new CommonResDto<>("리스트 수정 완료", response), HttpStatus.OK);
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            Authentication authentication
    ) {
        listService.deleteList(listId, authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
