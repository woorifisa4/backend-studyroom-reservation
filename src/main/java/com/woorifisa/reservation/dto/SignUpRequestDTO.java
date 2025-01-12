package com.woorifisa.reservation.dto;

import com.woorifisa.reservation.validation.annotation.Email;
import lombok.Data;

@Data
public class SignUpRequestDTO {
    private String name;

    @Email
    private String email;
}
