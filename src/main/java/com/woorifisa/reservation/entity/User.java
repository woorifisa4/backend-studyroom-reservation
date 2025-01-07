package com.woorifisa.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "user_name")
    private String name; // 사용자 이름

    @Column(name = "user_email")
    private String email; // 사용자 이메일

    @OneToMany(mappedBy = "reserver", cascade = CascadeType.ALL)
    private List<Reservation> reservations; // 예약 목록

    @OneToMany(mappedBy = "user")
    private List<ReservationParticipant> participations; // 참여 목록

}
