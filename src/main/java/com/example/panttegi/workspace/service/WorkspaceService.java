package com.example.panttegi.workspace.service;

import com.example.panttegi.enums.MemberRole;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.member.repository.MemberRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.workspace.dto.WorkspaceInviteResponseDto;
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
    public WorkspaceResponseDto createWorkspace(Workspace workspace, String email) {

        User user = userRepository.findByEmailOrElseThrow(email);

        workspace.updateUser(user);

        Workspace saveWorkspace = workspaceRepository.save(workspace);

        Member member = new Member(MemberRole.WORKSPACE, user, saveWorkspace);
        memberRepository.save(member);

        return new WorkspaceResponseDto(saveWorkspace);
    }

    public List<WorkspaceResponseDto> getAllWorkspaces(String email) {
        User user = userRepository.findByEmailOrElseThrow(email);

        List<Member> members = memberRepository.findByAllUserOrElseThrow(user);

        return members.stream()
                .map(member -> new WorkspaceResponseDto(member.getWorkspace()))
                .collect(Collectors.toList());
    }

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

        workspace.updateWorkspace(name, description);

        return new WorkspaceResponseDto(workspace);
    }

    @Transactional
    public void deleteWorkspace(Long workspaceId, String email) {
        User user = userRepository.findByEmailOrElseThrow(email);

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        workspaceRepository.delete(workspace);
    }

    @Transactional
    public WorkspaceInviteResponseDto inviteMember(Long workspaceId, String inviteEmail, MemberRole role, String inviterEmail) {
        User inviter = userRepository.findByEmailOrElseThrow(inviterEmail);
        User invitee = userRepository.findByEmailOrElseThrow(inviteEmail);

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        boolean alreadyMember = memberRepository.findByUserAndWorkspace(invitee, workspace).isPresent();
        if (alreadyMember) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Member newMember = memberRepository.save(new Member(role, invitee, workspace));

        return new WorkspaceInviteResponseDto(newMember);
    }
}
