package com.example.panttegi.card.service;

import com.example.panttegi.card.dto.CardRankingResponseDto;
import com.example.panttegi.card.dto.CardResponseDto;
import com.example.panttegi.card.entity.Card;
import com.example.panttegi.card.repository.CardRepository;
import com.example.panttegi.common.Const;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final ListRepository listRepository;

    private final StringRedisTemplate redisTemplate;
    private final ZSetOperations<String, String> zSetOperations;

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
    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId, String email) {

        updateCardViewCount(cardId, email);

        return new CardResponseDto(cardRepository.findByIdOrElseThrow(cardId));
    }

    // 카드 랭킹 조회
    public List<CardRankingResponseDto> getCardRanking(int limit) {
        Set<ZSetOperations.TypedTuple<String>> rankingSet = zSetOperations.reverseRangeWithScores(Const.CARD_VIEW_RANKING_KEY, 0, limit - 1);

        if (rankingSet == null || rankingSet.isEmpty()) {
            return List.of();
        }

        return rankingSet.stream().map(tuple -> {
            Long cardId = Long.parseLong(Objects.requireNonNull(tuple.getValue()));
            Card card = cardRepository.findByIdOrElseThrow(cardId);
            Double viewCount = Objects.requireNonNull(tuple.getScore());
            Long rank = zSetOperations.reverseRank(Const.CARD_VIEW_RANKING_KEY, tuple.getValue()) + 1;

            return new CardRankingResponseDto(new CardResponseDto(card), viewCount, rank);
        }).toList();
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

        String lockKey = "updateCard:lock" + cardId;

        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Const.LOCK_EXPIRATION_TIME, TimeUnit.MILLISECONDS);

        if (Boolean.FALSE.equals(lockAcquired)) {
            throw new CustomException(ErrorCode.CONCURRENCY_CONFLICT);
        }

        try {
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
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    // 카드 삭제
    public void deleteCard(Long cardId, String email) {
        userRepository.findByEmailOrElseThrow(email);

        Card card = cardRepository.findByIdOrElseThrow(cardId);

        cardRepository.delete(card);
    }

    private void updateCardViewCount(Long cardId, String userEmail) {
        String cardKey = Const.CARD_VIEW_KEY_PREFIX + cardId;
        String userKey = Const.CARD_USER_VIEW_KEY_PREFIX + cardId + ":" + userEmail;

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // 동일 유저가 동일 카드를 조회했는지 확인
        if (Boolean.TRUE.equals(redisTemplate.hasKey(userKey))) {
            return;
        }

        // 유저 조회 키 설정 (1일 동안 유효)
        ops.set(userKey, "viewed", 1, TimeUnit.DAYS);

        // 조회수 증가
        redisTemplate.boundValueOps(cardKey).increment(1);
        // 랭킹 갱신
        zSetOperations.incrementScore(Const.CARD_VIEW_RANKING_KEY, String.valueOf(cardId), 1);
    }

    public Long getCardViewCount(Long cardId) {
        String cardKey = Const.CARD_VIEW_KEY_PREFIX + cardId;
        String count = redisTemplate.opsForValue().get(cardKey);
        return count == null ? 0L : Long.parseLong(count);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    public void resetCardViewCounts() {
        for (String key : Objects.requireNonNull(redisTemplate.keys(Const.CARD_VIEW_KEY_PREFIX + "*"))) {
            redisTemplate.delete(key);
        }
        for (String key : Objects.requireNonNull(redisTemplate.keys(Const.CARD_USER_VIEW_KEY_PREFIX + "*"))) {
            redisTemplate.delete(key);
        }
        redisTemplate.delete(Const.CARD_VIEW_RANKING_KEY);
    }
}
