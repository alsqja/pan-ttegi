package com.example.panttegi.user.service;

import com.example.panttegi.error.errorcode.ErrorCode;
import com.example.panttegi.error.exception.CustomException;
import com.example.panttegi.user.dto.UserResponseDto;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public UserResponseDto signup(User user) {

        boolean isExist = userRepository.findByEmail(user.getEmail()).isPresent();

        if (isExist) {
            throw new CustomException(ErrorCode.BAD_INPUT);
        }

        return new UserResponseDto(userRepository.save(user));
    }
}
