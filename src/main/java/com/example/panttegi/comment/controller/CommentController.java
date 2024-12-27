package com.example.panttegi.comment.controller;

import com.example.panttegi.card.dto.CardResponseDto;
import com.example.panttegi.comment.dto.CommentRequestDto;
import com.example.panttegi.comment.dto.CommentResponseDto;
import com.example.panttegi.comment.dto.UpdateCommentRequestDto;
import com.example.panttegi.comment.service.CommentService;
import com.example.panttegi.common.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommonResDto<CommentResponseDto>> createComment(
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            Authentication authentication
            ) {

        CommentResponseDto comment = commentService.createComment(
                commentRequestDto.getCardId(),
                authentication.getName(),
                commentRequestDto.getContent()
        );

        return new ResponseEntity<>(new CommonResDto<>("댓글 생성 완료", comment), HttpStatus.CREATED);
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResDto<CommentResponseDto>> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequestDto updateCommentRequestDto,
            Authentication authentication
    ) {

        CommentResponseDto comment = commentService.updateComment(commentId, updateCommentRequestDto.getContent(), authentication.getName());

        return new ResponseEntity<>(new CommonResDto<>("댓글 수정 완료", comment), HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {

        commentService.deleteComment(commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
