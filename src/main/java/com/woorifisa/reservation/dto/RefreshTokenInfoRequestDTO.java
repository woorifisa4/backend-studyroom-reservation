package com.woorifisa.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenInfoRequestDTO {

    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    private String refreshToken;
}
