package com.example.panttegi.list.service;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.repository.BoardRepository;
import com.example.panttegi.list.dto.ListRequestDto;
import com.example.panttegi.list.dto.ListResponseDto;
import com.example.panttegi.list.entity.BoardList;
import com.example.panttegi.list.repository.ListRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public ListResponseDto createList(Long boardId, ListRequestDto listRequestDto, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        BoardList boardList = new BoardList(listRequestDto.getTitle(), listRequestDto.getPosition(), user, board);
        return new ListResponseDto(listRepository.save(boardList));
    }
}
