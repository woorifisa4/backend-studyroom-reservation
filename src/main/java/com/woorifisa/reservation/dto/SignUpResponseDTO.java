package com.woorifisa.reservation.dto;

import com.woorifisa.reservation.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
