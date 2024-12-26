package com.example.panttegi.member.repository;

import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserAndWorkspace(User user, Workspace workspace);

    @Query("SELECT m FROM Member m JOIN FETCH m.workspace WHERE m.user = :user")
    List<Member> findAllByUser(@Param("user") User user);

    default Member findByUserAndWorkspaceOrElseThrow(User user, Workspace workspace) {
        return findByUserAndWorkspace(user, workspace).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    default List<Member> findByAllUserOrElseThrow(User user) {
        List<Member> members = findAllByUser(user);
        if (members.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        return members;
    }
}
