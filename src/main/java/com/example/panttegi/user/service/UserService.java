package com.example.panttegi.user.service;

import com.example.panttegi.enums.UserRole;
import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.user.dto.UserResponseDto;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto updateUser(String name, String profileUrl, UserRole role, String email, Long id) {

        User user = userRepository.findByEmailOrElseThrow(email);

        if (!user.getId().equals(id)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        if (name != null) {
            user.updateName(name);
        }
        if (profileUrl != null) {
            user.updateProfileUrl(profileUrl);
        }
        if (role != null) {
            user.updateRole(role);
        }

        return new UserResponseDto(userRepository.save(user));
    }

    public void checkPassword(String password, String email) {

        User user = userRepository.findByEmailOrElseThrow(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }
    }
}
