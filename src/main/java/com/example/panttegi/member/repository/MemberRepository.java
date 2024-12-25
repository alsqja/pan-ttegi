package com.example.panttegi.member.repository;

import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserAndWorkspace(User user, Workspace workspace);

    default Member findByUserAndWorkspaceOrElseThrow(User user, Workspace workspace) {
        return findByUserAndWorkspace(user, workspace).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}
