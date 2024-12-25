package com.example.panttegi.board.service;

import com.example.panttegi.board.dto.BoardResDto;
import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.repository.BoardRepository;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.member.repository.MemberRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.workspace.entity.Workspace;
import com.example.panttegi.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardResDto createBoard(Board board, String email, Long workspaceId) {

        //  TODO: Refactor : member 를 fetchJoin 으로 불러오면 workspace db 호출 1회 줄일 수 있을까?
        User user = userRepository.findByEmailOrElseThrow(email);
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Member member = memberRepository.findByUserAndWorkspaceOrElseThrow(user, workspace);

        board.updateUser(user);
        board.updateWorkspace(workspace);

        return new BoardResDto(boardRepository.save(board));
    }
}
