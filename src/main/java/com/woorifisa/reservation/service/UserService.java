package com.woorifisa.reservation.service;

import com.woorifisa.reservation.dto.LoginRequestDTO;
import com.woorifisa.reservation.dto.LoginResponseDTO;
import com.woorifisa.reservation.dto.SignUpRequestDTO;
import com.woorifisa.reservation.dto.SignUpResponseDTO;
import com.woorifisa.reservation.entity.User;
import com.woorifisa.reservation.exception.AlreadyExistsEmailException;
import com.woorifisa.reservation.exception.InvalidUserInfoException;
import com.woorifisa.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public LoginResponseDTO login(LoginRequestDTO request) {
        Optional<User> foundUser = userRepository.findByEmail(request.getEmail());

        if (foundUser.isPresent() && foundUser.get().getName().equals(request.getName())) {
            return new LoginResponseDTO(foundUser.get());
        } else {
            throw new InvalidUserInfoException("올바르지 않은 정보입니다.");
        }
    }

    public SignUpResponseDTO signup(SignUpRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistsEmailException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        User savedUser = userRepository.save(user);
        return new SignUpResponseDTO(savedUser);
    }
}
