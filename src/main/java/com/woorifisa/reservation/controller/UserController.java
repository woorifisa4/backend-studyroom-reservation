package com.woorifisa.reservation.controller;

import com.woorifisa.reservation.dto.*;
import com.woorifisa.reservation.entity.User;
import com.woorifisa.reservation.repository.UserRepository;
import com.woorifisa.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse<SignUpResponseDTO> signup(@RequestBody SignUpRequestDTO request) {
        SignUpResponseDTO data = userService.signup(request);
        return new BaseResponse<>(201, "User created successfully", data);
    }

    @PostMapping("/login")
    public BaseResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO data = userService.login(request);
        return new BaseResponse<>(200, "Login successful", data);
    }
}
