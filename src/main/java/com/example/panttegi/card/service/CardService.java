package com.example.panttegi.card.service;

import com.example.panttegi.card.entity.Card;
import com.example.panttegi.card.dto.CardResponseDto;
import com.example.panttegi.card.repository.CardRepository;
import com.example.panttegi.file.repository.FileRepository;
import com.example.panttegi.file.repository.entity.File;
import com.example.panttegi.list.BoardList;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    // 카드 생성
    public CardResponseDto postCard(
            String title, String description, int position, LocalDateTime endAt,
            String email, Long managerId, Long listId, List<Long> fileIds
    ) {

        User user = userRepository.findByEmailOrElseThrow(email);
        User manager = userRepository.findByIdOrElseThrow(managerId);
        BoardList boardList = new BoardList(); // 추후 추가

        List<File> files = fileIds.stream()
                .map(fileRepository::findByIdOrElseThrow)
                .toList();

        Card card = new Card(title, description, position, endAt,
                user, manager, boardList, files);

        return new CardResponseDto(cardRepository.save(card));

    }

    // 카드 단일 조회
    public CardResponseDto getCard(Long cardId) {

        return new CardResponseDto(cardRepository.findByIdOrElseThrow(cardId));
    }

    // 카드 수정
    public void updateCard (
            Long cardId, String title, String description, int position, LocalDateTime endAt,
            String email, Long managerId, Long listId, List<Long> fileIds
    ) {
        cardRepository.findByIdOrElseThrow(cardId);

    }

    // 카드 삭제
    public void deleteCard(Long cardId, String email) {
        userRepository.findByEmailOrElseThrow(email);

        Card card = cardRepository.findByIdOrElseThrow(cardId);

        cardRepository.delete(card);
    }

}
