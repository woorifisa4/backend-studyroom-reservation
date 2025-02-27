package com.woorifisa.reservation.dto;

import com.woorifisa.reservation.entity.Reservation;
import com.woorifisa.reservation.entity.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetReservationInfoResponseDTO {
    private Long id; // 예약 식별자
    private Table table; // 예약된 회의실
    private LocalDate date; // 예약 날짜
    private LocalTime start; // 예약 시작 시간
    private LocalTime end; // 예약 종료 시간
    private String description; // 예약 설명
    private UserDTO reserver; // 예약한 사용자

    public GetReservationInfoResponseDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.table = reservation.getTable();
        this.date = reservation.getDate();
        this.start = reservation.getStart();
        this.end = reservation.getEnd();
        this.description = reservation.getDescription();
        this.reserver = new UserDTO(reservation.getReserver());
    }
}
