package com.woorifisa.reservation.service;

import com.woorifisa.reservation.dto.CreateReservationRequestDTO;
import com.woorifisa.reservation.dto.CreateReservationResponseDTO;
import com.woorifisa.reservation.entity.Reservation;
import com.woorifisa.reservation.entity.Table;
import com.woorifisa.reservation.entity.User;
import com.woorifisa.reservation.repository.ReservationParticipantRepository;
import com.woorifisa.reservation.repository.ReservationRepository;
import com.woorifisa.reservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationParticipantRepository reservationParticipantRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_reservation_success_1() {
        // Given: 들어올 예약 정보와 충돌이 없는 상황이 주어졌을때
        CreateReservationRequestDTO request = CreateReservationRequestDTO.builder()
                .table(Table.A)
                .date(LocalDate.now())
                .start(LocalTime.of(18, 0))
                .end(LocalTime.of(20, 0))
                .description("Test Reservation")
                .reserver(1L)
                .participants(List.of(2L))
                .build();

        User reserver = new User(1L, "Reserver", "reserver@example.com", null, null);
        User participant = new User(2L, "Participant", "participant@example.com", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(reserver));
        when(userRepository.findByIdIn(Collections.singletonList(2L))).thenReturn(Collections.singletonList(participant));
        when(reservationRepository.findByDate(request.getDate())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: 충돌이 없는 예약 요청이 들어오면
        CreateReservationResponseDTO response = reservationService.createReservation(request);

        // Then: 예약이 생성되고, 예약 정보가 반환된다
        assertNotNull(response);
        assertEquals(request.getTable(), response.getTable());
        assertEquals(request.getDate(), response.getDate());
        assertEquals(request.getStart(), response.getStart());
        assertEquals(request.getEnd(), response.getEnd());
        assertEquals(request.getDescription(), response.getDescription());
        verify(reservationRepository, times(1)).save(any(Reservation.class)); // save 메소드가 1번 호출되었는지 확인
    }

    @Test
    void create_reservation_success_2() {
        // Given: 들어올 예약 정보와 충돌이 없는 상황이 주어졌을때
        CreateReservationRequestDTO request = CreateReservationRequestDTO.builder()
                .table(Table.A)
                .date(LocalDate.now())
                .start(LocalTime.of(18, 0))
                .end(LocalTime.of(20, 0))
                .description("Test Reservation")
                .reserver(1L)
                .participants(List.of(2L))
                .build();

        User reserver = new User(1L, "Reserver", "reserver@example.com", null, null);
        User participant = new User(2L, "Participant", "participant@example.com", null, null);

        Reservation existingReservation = Reservation.builder()
                .table(Table.A)
                .date(LocalDate.now())
                .start(LocalTime.of(20, 00))
                .end(LocalTime.of(20, 30))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(reserver));
        when(userRepository.findByIdIn(Collections.singletonList(2L))).thenReturn(Collections.singletonList(participant));
        when(reservationRepository.findByDate(request.getDate())).thenReturn(Collections.singletonList(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: 충돌이 없는 예약 요청이 들어오면
        CreateReservationResponseDTO response = reservationService.createReservation(request);

        // Then: 예약이 생성되고, 예약 정보가 반환된다
        assertNotNull(response);
        assertEquals(request.getTable(), response.getTable());
        assertEquals(request.getDate(), response.getDate());
        assertEquals(request.getStart(), response.getStart());
        assertEquals(request.getEnd(), response.getEnd());
        assertEquals(request.getDescription(), response.getDescription());
        verify(reservationRepository, times(1)).save(any(Reservation.class)); // save 메소드가 1번 호출되었는지 확인
    }
}