package com.woorifisa.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 식별자

    /**
     * @see Reservation #id
     */
    @Column(name = "reservation_id")
    private Long reservationId; // 예약 식별자

    /**
     * @see User #id
     */
    @Column(name = "participant_user_id")
    private Long participantUserId; // 사용자 식별자
}