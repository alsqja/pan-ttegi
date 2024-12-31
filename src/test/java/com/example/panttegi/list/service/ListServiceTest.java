package com.example.panttegi.list.service;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.repository.BoardRepository;
import com.example.panttegi.enums.UserRole;
import com.example.panttegi.list.entity.BoardList;
import com.example.panttegi.list.repository.ListRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.workspace.entity.Workspace;
import com.example.panttegi.workspace.repository.WorkspaceRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Rollback
//@Transactional
class ListServiceTest {

    @Autowired
    ListRepository listRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ListService listService;

    @Autowired
    EntityManager em;

    @BeforeEach
    void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    @Test
    @DisplayName("리스트 수정 동시성 제어")
    void updateList() throws InterruptedException, ExecutionException {
        User testUser = new User("test@email.com", "password", "profileUrl", "name", UserRole.ADMIN);
        User savedUser = userRepository.save(testUser);
        Workspace testWorkspace = new Workspace("workspace", "description", savedUser);
        Workspace savedWorkspace = workspaceRepository.save(testWorkspace);
        Board testBoard = new Board("color", "url", "boardName", savedUser, savedWorkspace);
        Board savedBoard = boardRepository.save(testBoard);
        BoardList testBoardList = new BoardList("listTitle", BigDecimal.valueOf(100), savedUser, savedBoard);
        BoardList savedList = listRepository.save(testBoardList);

        BoardList targetList = listRepository.findById(savedList.getId())
                .orElseThrow(() -> new IllegalStateException("Saved list not found!"));

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        BoardList savedListCheck = listRepository.findById(savedList.getId())
                .orElseThrow(() -> new IllegalStateException("Saved list not found!"));
        System.out.println("Saved List: " + savedListCheck.getId());


        for (int i = 0; i < threadCount; i++) {
            tasks.add(() -> {
                try {
                    listService.updateList(savedWorkspace.getId(), savedList.getId(), "fetchTitle", 1, savedUser.getEmail());
                    return true;
                } catch (RuntimeException e) {
                    return false;
                }
            });
        }

        List<Future<Boolean>> results = executorService.invokeAll(tasks);

        int successCount = 0;
        int failureCount = 0;

        for (Future<Boolean> result : results) {
            if (result.get()) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        System.out.println("Success Count: " + successCount);
        System.out.println("Failure Count: " + failureCount);

        assertEquals(1, successCount, "동시에 하나의 요청만 성공해야 합니다.");
        assertEquals(threadCount - 1, failureCount, "나머지 요청은 충돌로 인해 실패해야 합니다.");

        executorService.shutdown();
    }
}