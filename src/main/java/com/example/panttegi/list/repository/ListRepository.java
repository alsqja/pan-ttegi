package com.example.panttegi.list.repository;

import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<BoardList, Long> {

    default BoardList findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    List<BoardList> findByBoardId(Long boardId);

    @Query("SELECT COUNT(l) FROM BoardList l WHERE l.board.id = :boardId")
    Long countByBoardId(@Param("boardId") Long boardId);
}
