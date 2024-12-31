package com.example.panttegi.user.service;

import com.example.panttegi.enums.AuthenticationScheme;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.user.dto.LoginResDto;
import com.example.panttegi.user.dto.TokenDto;
import com.example.panttegi.user.dto.UserResponseDto;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.RefreshTokenRepository;
import com.example.panttegi.user.repository.UserRepository;
import com.example.panttegi.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserResponseDto signup(User user) {

        boolean isExist = userRepository.findByEmail(user.getEmail()).isPresent();

        if (isExist) {
            throw new CustomException(ErrorCode.BAD_INPUT);
        }

        user.updatePassword(passwordEncoder.encode(user.getPassword()));

        return new UserResponseDto(userRepository.save(user));
    }

    public LoginResDto login(String email, String password) {

        User user = userRepository.findByEmailOrElseThrow(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        TokenDto tokens = jwtProvider.generateToken(authentication);

        return new LoginResDto(AuthenticationScheme.BEARER.getName(), tokens.getAccessToken(), tokens.getRefreshToken());
    }

    public TokenDto refresh(String accessToken, String refreshToken) {

        if (!jwtProvider.validRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        String email = jwtProvider.getUsername(refreshToken);
        String savedRequestToken = refreshTokenRepository.getRefreshToken(email);

        if (!refreshToken.equals(savedRequestToken)) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        return jwtProvider.generateToken(email);
    }
}
