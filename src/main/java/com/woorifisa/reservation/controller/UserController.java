package com.woorifisa.reservation.controller;

import com.woorifisa.reservation.dto.*;
import com.woorifisa.reservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignUpResponseDTO>> signup(@Valid @RequestBody SignUpRequestDTO body) {
        SignUpResponseDTO data = userService.signup(body);
        BaseResponse<SignUpResponseDTO> response = new BaseResponse<>(HttpStatus.CREATED.value(), "회원가입에 성공했습니다.", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO body) {
        LoginResponseDTO data = userService.login(body);
        BaseResponse<LoginResponseDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "로그인에 성공했습니다.", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<UserQueryResponseDTO>> searchUsers(@RequestParam String keyword) {
        UserQueryResponseDTO data = userService.searchUsers(keyword);
        BaseResponse<UserQueryResponseDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "사용자 검색에 성공했습니다.", data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<BaseResponse<TokenDTO>> refreshToken(@Valid @RequestBody RefreshTokenInfoRequestDTO body) {
        TokenDTO data = userService.refreshToken(body);
        BaseResponse<TokenDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "토큰 재발급에 성공했습니다.", data);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
