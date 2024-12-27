package com.example.panttegi.config.interceptor;

import com.example.panttegi.enums.MemberRole;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.member.repository.MemberRepository;
import com.example.panttegi.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.panttegi.config.interceptor.InterceptorUtils.extractPathVariable;
import static com.example.panttegi.config.interceptor.InterceptorUtils.getTokenFromRequest;

@Component
@RequiredArgsConstructor
public class WorkspaceRoleInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = getTokenFromRequest(request);

        if (!jwtProvider.validToken(token)) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        String email = jwtProvider.getUsername(token);
        Long workspaceId = Long.parseLong(extractPathVariable(request, "workspaceId"));

        if (!hasAccessWorkspace(email, workspaceId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        return true;
    }

    private boolean hasAccessWorkspace(String email, Long workspaceId) {
        Member member = memberRepository.findByEmailAndWorkspaceId(email, workspaceId);

        if (member == null) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        return member.getRole().equals(MemberRole.WORKSPACE);
    }
}