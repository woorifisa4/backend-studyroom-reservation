package com.woorifisa.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.woorifisa.reservation.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class TokenDTO {

    private String tokenType;

    private Role role;

    private String accessToken;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date accessTokenExpiresAt;

    private String refreshToken;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date refreshTokenExpiresAt;
}
