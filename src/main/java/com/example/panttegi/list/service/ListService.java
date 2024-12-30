package com.example.panttegi.list.service;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.repository.BoardRepository;
import com.example.panttegi.common.Const;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.list.dto.ListResponseDto;
import com.example.panttegi.list.entity.BoardList;
import com.example.panttegi.list.repository.ListRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.workspace.entity.Workspace;
import com.example.panttegi.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public ListResponseDto createList(Long workspaceId, Long boardId, String title, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        if (!board.getWorkspace().getId().equals(workspace.getId())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        List<BoardList> lists = listRepository.findByBoardId(boardId);
        lists.sort(Comparator.comparing(BoardList::getPosition));

        BigDecimal newPosition;

        if (lists.isEmpty()) {
            newPosition = BigDecimal.valueOf(100);
        } else {
            BigDecimal lastPosition = lists.get(lists.size() - 1).getPosition();
            newPosition = lastPosition.add(BigDecimal.valueOf(100));
        }

        BoardList boardList = new BoardList(title, newPosition, user, board);
        return new ListResponseDto(listRepository.save(boardList));
    }

    @Transactional
    public ListResponseDto updateList(Long workspaceId, Long listId, String title, int targetIndex, String email) {

        String lockKey = "updateList:lock" + listId;

        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", Const.LOCK_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        System.out.println("-----------------------------Lock Acquired: " + lockAcquired);

        if (Boolean.FALSE.equals(lockAcquired)) {
            throw new CustomException(ErrorCode.CONCURRENCY_CONFLICT);
        }

        try {
            BoardList targetList = listRepository.findByIdOrElseThrow(listId);

            if (!targetList.getBoard().getWorkspace().getId().equals(workspaceId)) {
                redisTemplate.delete(lockKey);
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }

            List<BoardList> lists = listRepository.findByBoardId(targetList.getBoard().getId());
            lists.sort(Comparator.comparing(BoardList::getPosition));

            targetIndex -= 1;

            if (targetIndex < 0 || targetIndex >= lists.size() + 1) {
                redisTemplate.delete(lockKey);
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }

            if (title != null) {
                targetList.updateTitle(title);
            }

            BigDecimal newPosition = calculateNewPosition(lists, targetIndex);
            targetList.updatePosition(newPosition);

            return new ListResponseDto(targetList);
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Transactional
    public void deleteList(Long workspaceId, Long listId, String email) {
        BoardList boardList = listRepository.findByIdOrElseThrow(listId);

        if (!boardList.getBoard().getWorkspace().getId().equals(workspaceId)) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        listRepository.delete(boardList);
    }

    private BigDecimal calculateNewPosition(List<BoardList> lists, int targetIndex) {

        if (lists.isEmpty()) {
            return BigDecimal.valueOf(100);
        }

        if (targetIndex == 0) {
            return lists.get(0).getPosition().subtract(BigDecimal.valueOf(100));
        }

        if (targetIndex >= lists.size()) {
            return lists.get(lists.size() - 1).getPosition().add(BigDecimal.valueOf(100));
        }

        BigDecimal prevPosition = lists.get(targetIndex - 1).getPosition();
        BigDecimal nextPosition = lists.get(targetIndex).getPosition();

        return prevPosition.add(nextPosition).divide(BigDecimal.valueOf(2), 6, RoundingMode.HALF_UP);
    }
}
