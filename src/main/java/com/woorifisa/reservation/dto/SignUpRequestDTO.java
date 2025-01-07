package com.woorifisa.reservation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequestDTO {
    private String name;
    private String email;
}
