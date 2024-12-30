package com.example.panttegi.config.aop;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.repository.BoardRepository;
import com.example.panttegi.card.entity.Card;
import com.example.panttegi.card.repository.CardRepository;
import com.example.panttegi.enums.UserRole;
import com.example.panttegi.list.entity.BoardList;
import com.example.panttegi.list.repository.ListRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.workspace.entity.Workspace;
import com.example.panttegi.workspace.repository.WorkspaceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QueryExecutionTimeAspect {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ListRepository listRepository;
    private final WorkspaceRepository workspaceRepository;
    private final BoardRepository boardRepository;

    @PostConstruct
    private void init() {

        int count = 100000;
        generateTestData(count);

        runTest(10);

    }

    // 카드 생성
    private void generateTestData(int count) {

        log.info("카드 생성 {} 개 시작", count);

        User user = userRepository.save(new User("asdf@asdf.com", "asdf", "asdf", "asdf", UserRole.USER));
        Workspace workspace = workspaceRepository.save(new Workspace("asdf", "asdf", user));
        Board board = boardRepository.save(new Board("asdf", "asdf", "asdf", user, workspace));
        BoardList list = listRepository.save(new BoardList("asdf", 1.0, user, board));

        for (int i = 0; i < count; i++) {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());

            String title = "title" + random.nextInt(10);
            String description = "description" + random.nextInt(10);
            String position = "position" + random.nextInt(10);
            LocalDateTime endAt = LocalDateTime.now();
            Card card = new Card(title, description, position, endAt, user, user, list);
            cardRepository.save(card);
        }

        log.info("카드 생성 완료");
    }

    // 테스트 돌리는 거
    private void runTest(int repeat) {
        log.info("{} 번째 성능 체크 시작", repeat);

        long totalTime = 0;

        for (int i = 0; i < repeat; i++) {
            long start = System.currentTimeMillis();

            cardRepository.searchByConditions(
              null,
              null,
              "1",
              "5",
              null,
                    PageRequest.of(0, 10)
            );

            long end = System.currentTimeMillis();
            long executionTime = end - start;
            totalTime += executionTime;

            log.info("{} 번째 실행, 실행시간: {} ms", i, executionTime);
        }

        long averageTime = totalTime / repeat;
        log.info("반복 횟수: {}, 걸린 평균시간: {} ms", repeat, averageTime);
    }


    @Around("execution(* com.example.panttegi.card.repository.CardRepository.searchByConditions(..))")
    public Object measureQueryExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // 메서드 실행

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        log.info("검색 시간: {} ms, 사용한 매서드: {}", executionTime, joinPoint.getSignature());

        return result; // 결과 반환
    }

}
