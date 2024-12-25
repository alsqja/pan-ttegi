package com.example.panttegi.user.controller;

import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.user.dto.LoginReqDto;
import com.example.panttegi.user.dto.LoginResDto;
import com.example.panttegi.user.dto.SignupRequestDto;
import com.example.panttegi.user.dto.UserResponseDto;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResDto<UserResponseDto>> signup(
            @Valid @RequestBody SignupRequestDto dto
    ) {

        User user = User.toEntity(dto);

        return new ResponseEntity<>(new CommonResDto<>("회원가입 완료", authService.signup(user)), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResDto<LoginResDto>> login(
            @RequestBody LoginReqDto dto
    ) {
        LoginResDto result = authService.login(dto.getEmail(), dto.getPassword());

        return new ResponseEntity<>(new CommonResDto<>("로그인 완료", result), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {

        if (authentication != null && authentication.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
