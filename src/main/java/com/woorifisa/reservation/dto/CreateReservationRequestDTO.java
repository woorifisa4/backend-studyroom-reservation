package com.woorifisa.reservation.dto;

import com.woorifisa.reservation.entity.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequestDTO {
    private Table table; // 예약된 회의실
    private LocalDate date; // 예약 날짜
    private LocalTime start; // 예약 시작 시간
    private LocalTime end; // 예약 종료 시간
    private String description; // 예약 설명
    private Long reserver; // 예약한 사용자
    private List<Long> participants; // 참여자 목록
}
