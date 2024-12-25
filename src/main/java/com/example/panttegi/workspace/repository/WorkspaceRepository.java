package com.example.panttegi.workspace.repository;

import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    default Workspace findByIdOrElseThrow(Long workspaceId) {
        return findById(workspaceId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
