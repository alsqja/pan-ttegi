package com.example.panttegi.user.controller;

import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.user.dto.SignupRequestDto;
import com.example.panttegi.user.dto.UserResponseDto;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
