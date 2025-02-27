package com.woorifisa.reservation.entity;

import com.woorifisa.reservation.validation.annotation.Name;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 식별자

    @Name
    @Column(name = "user_name")
    private String name; // 사용자 이름

    @Email
    @Column(name = "user_email")
    private String email; // 사용자 이메일

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @Builder.Default
    private Role role = Role.USER;

    @OneToMany(mappedBy = "reserver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations; // 예약 목록

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationParticipant> participations = new ArrayList<>();
}