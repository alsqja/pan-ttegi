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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 카드 조회
    public Page<CardResponseDto> searchCard(Long workspaceId, Long boardId, String title,
                                            String description, String managerName, int page
    ) {

        Pageable pageable = PageRequest.of(page, 10);

        return cardRepository.searchByConditions(workspaceId, boardId, title, description, managerName, pageable)
                .map(CardResponseDto::new);
    }

    // 카드 수정 (리스트 머지하면 수정, 포지션도)
    @Transactional
    public CardResponseDto updateCard (
            Long cardId, String title, String description, int position, LocalDateTime endAt,
            String email, Long managerId, Long listId, List<Long> fileIds
    ) {

        Card card = cardRepository.findByIdOrElseThrow(cardId);
        User user = userRepository.findByEmailOrElseThrow(email);
        User manager = userRepository.findByIdOrElseThrow(managerId);
        BoardList boardList = new BoardList(); // 추후 추가
        List<File> files = fileIds.stream()
                .map(fileRepository::findByIdOrElseThrow)
                .toList();

        card.updateTitle(title);
        card.updateDescription(description);
        card.updatePosition(position);
        card.updateEndAt(endAt);
        card.updateManager(manager);
        card.updateBoardList(boardList);
        card.updateFiles(files);

        return new CardResponseDto(cardRepository.save(card));
    }

    // 카드 삭제
    public void deleteCard(Long cardId, String email) {
        userRepository.findByEmailOrElseThrow(email);

        Card card = cardRepository.findByIdOrElseThrow(cardId);

        cardRepository.delete(card);
    }

}
