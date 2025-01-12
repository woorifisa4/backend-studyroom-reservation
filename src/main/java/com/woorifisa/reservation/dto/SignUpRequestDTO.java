package com.woorifisa.reservation.dto;

import com.woorifisa.reservation.validation.annotation.Email;
import com.woorifisa.reservation.validation.annotation.Name;
import lombok.Data;

@Data
public class SignUpRequestDTO {

    @Name
    private String name;

    @Email
    private String email;
}
