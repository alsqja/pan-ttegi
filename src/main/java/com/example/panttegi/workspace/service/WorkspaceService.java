package com.example.panttegi.workspace.service;

import com.example.panttegi.enums.MemberRole;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.member.Member;
import com.example.panttegi.member.repository.MemberRepository;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.workspace.dto.WorkspaceResponseDto;
import com.example.panttegi.workspace.entity.Workspace;
import com.example.panttegi.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public WorkspaceResponseDto createWorkspace(String name, String description, Long userId){
        User user = userRepository.findByIdOrThrow(userId);

        if(!user.getRole().name().equals("ADMIN")){
            throw new CustomException(ErrorCode.UNAUTHORIZED_PERMISSION);
        }

        Workspace workspace = workspaceRepository.save(
                new Workspace(name, description, user)
        );

        Member member = new Member(MemberRole.WORKSPACE, user, workspace);
        memberRepository.save(member);

        return new WorkspaceResponseDto(workspace);
    }
}
