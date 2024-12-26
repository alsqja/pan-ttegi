package com.example.panttegi.list.service;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.repository.BoardRepository;
import com.example.panttegi.list.dto.ListResponseDto;
import com.example.panttegi.list.entity.BoardList;
import com.example.panttegi.list.repository.ListRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public ListResponseDto createList(Long boardId, String title, Double position, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        BoardList boardList = new BoardList(title, position, user, board);
        return new ListResponseDto(listRepository.save(boardList));
    }

    @Transactional
    public ListResponseDto updateList(Long listId, String title, Double position, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);

        BoardList boardList = listRepository.findByIdOrElseThrow(listId);

        boardList.updateTitle(title);

        List<BoardList> lists = listRepository.findByBoardId(boardList.getBoard().getId());
        lists.sort(Comparator.comparing(BoardList::getPosition));

        Double newPosition = calculateNewPosition(lists, position, boardList.getId());
        boardList.updatePosition(newPosition);

        return new ListResponseDto(boardList);
    }

    @Transactional
    public void deleteList(Long listId, String email) {
        BoardList boardList = listRepository.findByIdOrElseThrow(listId);
        listRepository.delete(boardList);
    }

    private Double calculateNewPosition(List<BoardList> lists, Double requestedPosition, Long currentListId) {
        int index = 0;

        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getPosition() > requestedPosition) {
                index = i;
                break;
            }
        }

        if (index == 0 || index >= lists.size()) {
            return requestedPosition;
        }

        Double prePosition = index > 0 ? lists.get(index - 1).getPosition() : 0.0;
        Double nextPosition = lists.get(index).getPosition();

        return (prePosition + nextPosition) / 2;
    }
}
