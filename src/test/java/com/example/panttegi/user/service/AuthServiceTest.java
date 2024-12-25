package com.example.panttegi.user.service;

import com.example.panttegi.enums.UserRole;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;

    String TEST_EMAIL = "testEmail@email.com";
    String TEST_NAME = "testName";
    String TEST_PROFILE = "testProfile";
    String TEST_PASSWORD = "test@Password";
    UserRole TEST_ROLE = UserRole.USER;

    @Test
    @DisplayName("회원가입 중복 이메일 예외 throw")
    void signup() {

        User wrongMockUserToService = new User();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        CustomException e = assertThrows(CustomException.class, () -> authService.signup(wrongMockUserToService));
        assertEquals(ErrorCode.BAD_INPUT, e.getErrorCode());
    }
}