package com.woorifisa.reservation.dto;

import com.woorifisa.reservation.entity.User;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private Long id;
    private String name;
    private String email;
    private TokenDTO token;

    public LoginResponseDTO(User user, TokenDTO token) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.token = token;
    }
}
