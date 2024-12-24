package com.example.panttegi.user.controller;

import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.user.dto.UpdateUserReqDto;
import com.example.panttegi.user.dto.UserResponseDto;
import com.example.panttegi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto<UserResponseDto>> updateUser(
            @RequestBody UpdateUserReqDto dto,
            @PathVariable Long id
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserResponseDto result = userService.updateUser(dto.getName(), dto.getProfileUrl(), dto.getRole(), authentication.getName(), id);

        return new ResponseEntity<>(new CommonResDto<>("회원 정보 수정 완료", result), HttpStatus.OK);
    }
}
