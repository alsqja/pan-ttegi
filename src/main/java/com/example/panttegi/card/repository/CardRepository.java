package com.example.panttegi.card.repository;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.card.entity.Card;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    default Card findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    @Query("SELECT c FROM Card c WHERE " +
            "(:workspaceId IS NULL OR c.boardList.board.workspace.id = :workspaceId) AND " +
            "(:boardId IS NULL OR c.boardList.board.id = :boardId) AND " +
            "(:title IS NULL OR c.title LIKE %:title%) AND " +
            "(:description IS NULL OR c.description LIKE %:description%) AND " +
            "(:managerName IS NULL OR c.manager.name LIKE %:managerName%)")
    Page<Card> searchCards(
            @Param("workspaceId") Long workspaceId,
            @Param("boardId") Long boardId,
            @Param("title") String title,
            @Param("description") String description,
            @Param("managerName") String managerName,
            Pageable pageable
    );

}
