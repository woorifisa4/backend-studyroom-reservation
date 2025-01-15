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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_user_id")
    private User participant;

    // 연관관계 편의 메서드
    public void setReservation(Reservation reservation) {
        if (this.reservation != null) {
            this.reservation.getParticipants().remove(this);
        }
        this.reservation = reservation;
        if (reservation != null) {
            reservation.getParticipants().add(this);
        }
    }

    public void setParticipant(User participant) {
        if (this.participant != null) {
            this.participant.getParticipations().remove(this);
        }
        this.participant = participant;
        if (participant != null) {
            participant.getParticipations().add(this);
        }
    }
}