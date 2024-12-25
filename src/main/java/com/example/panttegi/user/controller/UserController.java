package com.example.panttegi.user.controller;

import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.common.Const;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.user.dto.PasswordReqDto;
import com.example.panttegi.user.dto.UpdateUserReqDto;
import com.example.panttegi.user.dto.UserResponseDto;
import com.example.panttegi.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto<UserResponseDto>> updateUser(
            @RequestBody UpdateUserReqDto dto,
            @PathVariable Long id,
            Authentication authentication
    ) {

        UserResponseDto result = userService.updateUser(dto.getName(), dto.getProfileUrl(), dto.getRole(), authentication.getName(), id);

        return new ResponseEntity<>(new CommonResDto<>("회원 정보 수정 완료", result), HttpStatus.OK);
    }

    @PostMapping("/check-password")
    public ResponseEntity<Void> checkPassword(
            @RequestBody PasswordReqDto dto,
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        userService.checkPassword(dto.getPassword(), authentication.getName());

        HttpSession session = request.getSession();
        session.setAttribute(Const.PASSWORD_CHECK, Boolean.TRUE);

        // 쿠키 만료 시간 설정 (5분)
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setMaxAge(5 * 60); // 5분 (단위: 초)
        cookie.setHttpOnly(true); // 보안 강화
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @SessionAttribute(name = Const.PASSWORD_CHECK, required = false) Boolean isChecked,
            Authentication authentication
    ) {

        if (isChecked == null) {
            throw new CustomException(ErrorCode.UNCHECKED_PASSWORD);
        }

        userService.deleteUser(id, authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
