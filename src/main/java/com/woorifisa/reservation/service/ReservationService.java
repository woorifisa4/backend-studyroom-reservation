package com.woorifisa.reservation.service;

import com.woorifisa.reservation.dto.CreateReservationRequestDTO;
import com.woorifisa.reservation.dto.CreateReservationResponseDTO;
import com.woorifisa.reservation.dto.GetReservationInfoResponseDTO;
import com.woorifisa.reservation.entity.Reservation;
import com.woorifisa.reservation.entity.ReservationParticipant;
import com.woorifisa.reservation.entity.User;
import com.woorifisa.reservation.exception.ReservationConflictException;
import com.woorifisa.reservation.exception.ReservationNotFoundException;
import com.woorifisa.reservation.repository.ReservationParticipantRepository;
import com.woorifisa.reservation.repository.ReservationRepository;
import com.woorifisa.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final ReservationParticipantRepository reservationParticipantRepository;

    public List<GetReservationInfoResponseDTO> getReservationsByDate(LocalDate date) {
        List<Reservation> result = reservationRepository.findByDate(date);
        log.info("{} 날짜의 예약 목록 조회 완료 ({}건)", date, result.size());

        return result.stream()
                .map(GetReservationInfoResponseDTO::new)
                .toList();
    }

    public CreateReservationResponseDTO createReservation(CreateReservationRequestDTO requestDTO) {
        User reserver = userRepository.findById(requestDTO.getReserver())
                .orElseThrow(() -> new ReservationNotFoundException("예약한 사용자를 찾을 수 없습니다."));

        List<User> participants = userRepository.findByIdIn(requestDTO.getParticipants());

        Reservation reservation = Reservation.builder()
                .room(requestDTO.getRoom())
                .date(requestDTO.getDate())
                .start(requestDTO.getStart())
                .end(requestDTO.getEnd())
                .description(requestDTO.getDescription())
                .reserver(reserver)
                .participants(participants.stream()
                        .map(user -> new ReservationParticipant(null, user))
                        .collect(Collectors.toList()))
                .build();

        validateReservation(reservation);

        Reservation savedReservation = reservationRepository.save(reservation);
        participants.forEach(user ->
                reservationParticipantRepository.save(new ReservationParticipant(savedReservation, user))
        );
        log.info("예약 생성 완료 (예약 정보: \"{예약 날짜: {}, 시작 시간: {}, 종료 시간: {}, 회의실: {}, 예약자: {}}\")", reservation.getDate(), reservation.getStart(), reservation.getEnd(), reservation.getRoom(), reservation.getReserver());

        return new CreateReservationResponseDTO(savedReservation);
    }

    private void validateReservation(Reservation reservation) throws ReservationConflictException {
        List<Reservation> existingReservations = reservationRepository.findByDate(reservation.getDate());

        for (Reservation existingReservation : existingReservations) {
            if (isConflict(existingReservation, reservation)) {
                log.error("예약 충돌: {}", reservation);
                throw new ReservationConflictException("해당하는 시간대에 이미 예약이 있습니다.");
            }
        }
    }

    private boolean isConflict(Reservation existing, Reservation newReservation) {
        return existing.getRoom().equals(newReservation.getRoom()) // 같은 회의실이면서
                && ((existing.getStart().isBefore(newReservation.getEnd()) && existing.getEnd().isAfter(newReservation.getStart()))); // 시간대가 겹치는 경우
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            log.warn("예약 삭제 실패: {}", id);
            throw new ReservationNotFoundException("예약이 존재하지 않아 삭제에 실패하였습니다.");
        }

        reservationRepository.deleteById(id);
        log.info("예약 삭제 완료: {}", id);
    }
}
