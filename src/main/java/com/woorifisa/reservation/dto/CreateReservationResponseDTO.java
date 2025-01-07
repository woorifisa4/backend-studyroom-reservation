package com.woorifisa.reservation.dto;

import com.woorifisa.reservation.entity.Reservation;
import com.woorifisa.reservation.entity.Room;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CreateReservationResponseDTO {
    private Long id; // 예약 식별자
    private Room room; // 예약된 회의실
    private LocalDate date; // 예약 날짜
    private LocalTime start; // 예약 시작 시간
    private LocalTime end; // 예약 종료 시간
    private String description; // 예약 설명
    private UserDTO reserver; // 예약한 사용자
    private List<UserDTO> participants; // 참여자 목록

    public CreateReservationResponseDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.room = reservation.getRoom();
        this.date = reservation.getDate();
        this.start = reservation.getStart();
        this.end = reservation.getEnd();
        this.description = reservation.getDescription();
        this.reserver = new UserDTO(reservation.getReserver());
        this.participants = reservation.getParticipants().stream()
                .map(participant -> new UserDTO(participant.getUser()))
                .toList();
    }
}
