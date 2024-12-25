package com.example.panttegi.workspace.service;

import com.example.panttegi.enums.MemberRole;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.member.repository.MemberRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.workspace.dto.WorkspaceResponseDto;
import com.example.panttegi.workspace.entity.Workspace;
import com.example.panttegi.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public WorkspaceResponseDto createWorkspace(String name, String description, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);

        if (!user.getRole().name().equals("ADMIN")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PERMISSION);
        }

        Workspace workspace = workspaceRepository.save(
                new Workspace(name, description, user)
        );

        Member member = new Member(MemberRole.WORKSPACE, user, workspace);
        memberRepository.save(member);

        return new WorkspaceResponseDto(workspace);
    }

    @Transactional
    public List<WorkspaceResponseDto> getAllWorkspaces(String email) {
        User user = userRepository.findByEmailOrElseThrow(email);

        List<Member> members = memberRepository.findByAllUserOrElseThrow(user);

        return members.stream()
                .map(member -> new WorkspaceResponseDto(member.getWorkspace()))
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkspaceResponseDto getWorkspace(Long workspaceId, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        Member member = memberRepository.findByUserAndWorkspaceOrElseThrow(user, workspace);

        return new WorkspaceResponseDto(workspace);
    }

    @Transactional
    public WorkspaceResponseDto updateWorkspace(Long workspaceId, String name, String description, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        Member member = memberRepository.findByUserAndWorkspaceOrElseThrow(user, workspace);
        if (!member.getRole().equals(MemberRole.WORKSPACE)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PERMISSION);
        }

        workspace.workspaceUpdate(name, description);

        return new WorkspaceResponseDto(workspace);
    }
}
