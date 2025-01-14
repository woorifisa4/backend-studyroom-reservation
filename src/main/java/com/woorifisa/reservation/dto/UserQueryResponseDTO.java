package com.woorifisa.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserQueryResponseDTO {
    private List<UserDTO> users;
}
