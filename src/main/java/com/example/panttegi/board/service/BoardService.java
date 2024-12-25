package com.example.panttegi.board.service;

import com.example.panttegi.board.dto.BoardResDto;
import com.example.panttegi.board.entity.Board;
import com.example.panttegi.board.model.BoardForUpdate;
import com.example.panttegi.board.repository.BoardRepository;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
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

        //  TODO: Refactor : member 를 fetchJoin 으로 불러오면 workspace db 호출 1회 줄일 수 있을까? OR email 을 활용해 유저 호출도 생략 가능?
        User user = userRepository.findByEmailOrElseThrow(email);
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        Member member = memberRepository.findByUserAndWorkspaceOrElseThrow(user, workspace);

        board.updateUser(user);
        board.updateWorkspace(workspace);

        return new BoardResDto(boardRepository.save(board));
    }

    @Transactional
    public BoardResDto updateBoard(BoardForUpdate boardForUpdate) {

        //  TODO: Refactor : fetch join 쿼리 조절
        Board board = boardRepository.findByEmailAndWorkspaceAndId(boardForUpdate.getEmail(), boardForUpdate.getWorkspaceId(), boardForUpdate.getBoardId());

        if (board == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        board.patchField(boardForUpdate.getColor(), boardForUpdate.getImageUrl(), boardForUpdate.getName());

        return new BoardResDto(boardRepository.save(board));
    }

    public void deleteBoard(Long workspaceId, Long boardId, String email) {

        Board board = boardRepository.findByEmailAndWorkspaceAndId(email, workspaceId, boardId);

        if (board == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        boardRepository.delete(board);
    }
}
