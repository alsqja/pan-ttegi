package com.example.panttegi.user.service;

import com.example.panttegi.enums.UserRole;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.user.dto.SignupRequestDto;
import com.example.panttegi.user.dto.UserResponseDto;
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
import static org.mockito.ArgumentMatchers.eq;
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
    @DisplayName("회원가입 서비스 단위 테스트")
    void signup() {

        SignupRequestDto dto = new SignupRequestDto(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_PROFILE, TEST_ROLE);
        SignupRequestDto wrongDto = new SignupRequestDto(TEST_EMAIL + "q", TEST_PASSWORD, TEST_NAME, TEST_PROFILE, TEST_ROLE);
        User wrongMockUserToService = User.toEntity(wrongDto);
        User mockUserToService = User.toEntity(dto);
        User mockUser = new User(1L, TEST_EMAIL, TEST_PASSWORD, TEST_PROFILE, TEST_NAME, TEST_ROLE);
        when(userRepository.findByEmail(eq(TEST_EMAIL))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(eq(TEST_EMAIL + "q"))).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponseDto findUser = authService.signup(mockUserToService);

        CustomException e = assertThrows(CustomException.class, () -> authService.signup(wrongMockUserToService));
        assertEquals(TEST_EMAIL, findUser.getEmail());
        assertEquals(ErrorCode.BAD_INPUT, e.getErrorCode());
    }
}