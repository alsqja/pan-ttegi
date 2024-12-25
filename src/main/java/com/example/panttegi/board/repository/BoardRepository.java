package com.example.panttegi.board.repository;

import com.example.panttegi.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("""
            select b from Board b
            join fetch User u on b.user.email = :email
            join fetch Workspace w on b.workspace.id = :workspaceId
            where b.id = :boardId
            """)
    Board findByEmailAndWorkspaceAndId(String email, Long workspaceId, Long boardId);
}
