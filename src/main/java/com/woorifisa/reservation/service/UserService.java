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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidUserInfoException("올바르지 않은 정보입니다."));

        if (!user.getName().equals(request.getName())) {
            log.warn("올바르지 않은 정보로 로그인을 시도합니다. ({}, {})", user.getName(), user.getEmail());
            throw new InvalidUserInfoException("올바르지 않은 정보입니다.");
        }

        log.info("사용자({}, {})가 로그인에 성공했습니다.", user.getName(), user.getEmail());

        return new LoginResponseDTO(user);
    }

    public SignUpResponseDTO signup(SignUpRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("존재하는 이메일로 회원가입을 시도합니다. ({})", request.getEmail());
            throw new AlreadyExistsEmailException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        User savedUser = userRepository.save(user);
        log.info("사용자 ({}, {})가 회원가입에 성공했습니다.", savedUser.getName(), savedUser.getEmail());

        return new SignUpResponseDTO(savedUser);
    }

}
