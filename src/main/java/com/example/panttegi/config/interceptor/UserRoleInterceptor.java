package com.example.panttegi.config.interceptor;

import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.member.repository.MemberRepository;
import com.example.panttegi.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.panttegi.config.interceptor.InterceptorUtils.extractPathVariable;

@Component
@RequiredArgsConstructor
public class UserRoleInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handleer) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        Long workspaceId = Long.parseLong(extractPathVariable(request));

        Member member = memberRepository.findByEmailAndWorkspaceId(email, workspaceId);

        if (member == null) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        return true;
    }
}
