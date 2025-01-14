package com.woorifisa.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예약 식별자

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_room")
    private Room room; // 예약된 회의실

    @Column(name = "reservation_date")
    private LocalDate date; // 예약 날짜

    @Column(name = "reservation_start_time")
    private LocalTime start; // 예약 시작 시간

    @Column(name = "reservation_end_time")
    private LocalTime end; // 예약 종료 시간

    @Column(name = "reservation_description")
    private String description; // 예약 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_reserver_id")
    private User reserver; // 예약한 사용자

}