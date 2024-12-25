package com.example.panttegi.board.controller;

import com.example.panttegi.board.dto.BoardReqDto;
import com.example.panttegi.board.dto.BoardResDto;
import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.model.BoardForUpdate;
import com.example.panttegi.board.service.BoardService;
import com.example.panttegi.common.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<CommonResDto<BoardResDto>> createBoard(
            @Valid @RequestBody BoardReqDto dto,
            @PathVariable Long workspaceId,
            Authentication authentication
    ) {

        Board board = dto.toEntity();
        BoardResDto result = boardService.createBoard(board, authentication.getName(), workspaceId);

        return new ResponseEntity<>(new CommonResDto<>("보드 생성 완료", result), HttpStatus.CREATED);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<CommonResDto<BoardResDto>> updateBoard(
            @RequestBody BoardReqDto dto,
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            Authentication authentication
    ) {

        BoardForUpdate boardForUpdate = new BoardForUpdate(dto, boardId, workspaceId, authentication.getName());
        BoardResDto result = boardService.updateBoard(boardForUpdate);

        return new ResponseEntity<>(new CommonResDto<>("보드 수정 완료", result), HttpStatus.OK);
    }
}
