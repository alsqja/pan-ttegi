package com.example.panttegi.card.service;

import com.example.panttegi.card.entity.Card;
import com.example.panttegi.card.dto.PostCardResponseDto;
import com.example.panttegi.card.repository.CardRepository;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.list.BoardList;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    // 카드 생성
    public PostCardResponseDto postCard(
            String title, String description, int position, LocalDateTime endAt,
            Long userId, Long managerId, Long listId
    ) {

        User user = userRepository.findByIdOrElseThrow(userId);
        User manager = userRepository.findByIdOrElseThrow(managerId);
        BoardList boardList = new BoardList(); // 추후 추가

        Card card = new Card(title, description, position, endAt, user, manager, boardList);

        return new PostCardResponseDto(cardRepository.save(card));

    }
}
