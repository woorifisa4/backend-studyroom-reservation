package com.woorifisa.reservation.dto;

import com.woorifisa.reservation.entity.User;
import lombok.Data;

@Data
public class SignUpResponseDTO {
    private Long id;
    private String name;
    private String email;

    public SignUpResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
