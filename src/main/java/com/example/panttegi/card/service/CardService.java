package com.example.panttegi.card.service;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.repository.BoardRepository;
import com.example.panttegi.card.dto.CardResponseDto;
import com.example.panttegi.card.entity.Card;
import com.example.panttegi.card.repository.CardRepository;
import com.example.panttegi.file.entity.File;
import com.example.panttegi.file.repository.FileRepository;
import com.example.panttegi.list.entity.BoardList;
import com.example.panttegi.list.repository.ListRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.util.LexoRank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ListRepository listRepository;

    // 카드 생성
    public CardResponseDto postCard(
            String title, String description, Long beforeCardId, Long afterCardId, LocalDateTime endAt,
            String email, Long managerId, Long listId, List<Long> fileIds
    ) {

        User user = userRepository.findByEmailOrElseThrow(email);
        User manager = userRepository.findByIdOrElseThrow(managerId);
        BoardList boardList = listRepository.findByIdOrElseThrow(listId);
        Card beforeCard = beforeCardId != 0 ? cardRepository.findByIdOrElseThrow(beforeCardId) : null;
        Card afterCard = afterCardId != 0 ? cardRepository.findByIdOrElseThrow(afterCardId) : null;

        List<File> files = fileIds.stream()
                .map(fileRepository::findByIdOrElseThrow)
                .toList();

        String position = LexoRank.getMiddleRank(
                beforeCard != null ? beforeCard.getPosition() : null,
                afterCard != null ? afterCard.getPosition() : null);

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

    // 카드 수정
    @Transactional
    public CardResponseDto updateCard(
            Long cardId, String title, String description, Long beforeCardId, Long afterCardId, LocalDateTime endAt,
            String email, Long managerId, Long listId, List<Long> fileIds
    ) {

        Card card = cardRepository.findByIdOrElseThrow(cardId);
        Card beforeCard = beforeCardId != 0 ? cardRepository.findByIdOrElseThrow(beforeCardId) : null;
        Card afterCard = afterCardId != 0 ? cardRepository.findByIdOrElseThrow(afterCardId) : null;
        User user = userRepository.findByEmailOrElseThrow(email);
        User manager = userRepository.findByIdOrElseThrow(managerId);
        BoardList boardList = listRepository.findByIdOrElseThrow(listId);
        List<File> files = fileIds.stream()
                .map(fileRepository::findByIdOrElseThrow)
                .toList();

        card.updateTitle(title);
        card.updateDescription(description);
        card.updatePosition(LexoRank.getMiddleRank(
                beforeCard != null ? beforeCard.getPosition() : null,
                afterCard != null ? afterCard.getPosition() : null));
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
