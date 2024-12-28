package com.example.panttegi.file.repository;

import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    default File findByIdOrElseThrow(long id) {
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    List<File> findAllByCardId(Long cardId);
}
