package com.example.panttegi.card.repository;

import com.example.panttegi.card.entity.Card;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    default Card findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    @Query("SELECT c FROM Card c " +
            "JOIN c.user u " +
            "JOIN c.manager m " +
            "JOIN c.boardList b " +
            "WHERE (:workspaceId IS NULL OR b.board.workspace.id = :workspaceId) " +
            "AND (:boardId IS NULL OR b.board.id = :boardId) " +
            "AND (:title IS NULL OR c.title LIKE %:title%) " +
            "AND (:description IS NULL OR c.description LIKE %:description%) " +
            "AND (:managerName IS NULL OR m.name LIKE %:managerName%)")
    Page<Card> searchByConditions(
            @Param("workspaceId") Long workspaceId,
            @Param("boardId") Long boardId,
            @Param("title") String title,
            @Param("description") String description,
            @Param("managerName") String managerName,
            Pageable pageable);

    Card findTopByOrderByPositionDesc();

    @Query("""
                SELECT c.position FROM Card c
                JOIN FETCH c.boardList l
                WHERE l.id = :listId
                ORDER BY c.position
            """)
    List<String> findLastPosition(Long listId);
}
