package com.example.panttegi.board.repository;

import com.example.panttegi.board.dto.BoardData;
import com.example.panttegi.board.entity.Board;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("""
            select b from Board b
            join fetch User u on b.user.email = :email
            join fetch Workspace w on b.workspace.id = :workspaceId
            where b.id = :boardId
            """)
    Board findByEmailAndWorkspaceAndId(String email, Long workspaceId, Long boardId);

    @Query("""
                SELECT new com.example.panttegi.board.dto.BoardData(
                    b.id, b.name, b.color, b.imageUrl,
                    l.id, l.title, l.position,
                    c.id, c.title, c.description, c.position, c.endAt,
                    u.name, c.manager.id,
                    f.id, f.url
                )
                FROM Board b
                LEFT JOIN b.boardLists l
                LEFT JOIN l.cards c
                LEFT JOIN c.manager u
                LEFT JOIN c.files f
                WHERE b.id = :boardId
            """)
    List<BoardData> getBoardDetails(@Param("boardId") Long boardId);

    default Board findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
