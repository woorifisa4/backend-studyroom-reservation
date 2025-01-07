package com.woorifisa.reservation.repository;

import com.woorifisa.reservation.entity.ReservationParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationParticipantRepository extends JpaRepository<ReservationParticipant, Long> {
}
