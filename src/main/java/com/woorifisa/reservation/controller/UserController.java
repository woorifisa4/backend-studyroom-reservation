package com.woorifisa.reservation.controller;

import com.woorifisa.reservation.dto.*;
import com.woorifisa.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignUpResponseDTO>> signup(@RequestBody SignUpRequestDTO body) {
        SignUpResponseDTO data = userService.signup(body);
        BaseResponse<SignUpResponseDTO> response = new BaseResponse<>(HttpStatus.CREATED.value(), "회원가입에 성공했습니다.", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO body) {
        LoginResponseDTO data = userService.login(body);
        BaseResponse<LoginResponseDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "로그인에 성공했습니다.", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
