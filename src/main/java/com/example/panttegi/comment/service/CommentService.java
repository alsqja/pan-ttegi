package com.example.panttegi.comment.service;

import com.example.panttegi.card.entity.Card;
import com.example.panttegi.card.repository.CardRepository;
import com.example.panttegi.comment.dto.CommentResponseDto;
import com.example.panttegi.comment.entity.Comment;
import com.example.panttegi.comment.repository.CommentRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    // 댓글 생성
    public CommentResponseDto createComment(Long cardId, String email, String content) {

        Card card = cardRepository.findByIdOrElseThrow(cardId);
        User user = userRepository.findByEmailOrElseThrow(email);

        Comment comment = new Comment(content, user, card);

        return new CommentResponseDto(commentRepository.save(comment));
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, String content, String email) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        return new CommentResponseDto(commentRepository.save(comment.updateContent(content)));
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, String email) {
        userRepository.findByEmailOrElseThrow(email);

        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        commentRepository.delete(comment);
    }

}
