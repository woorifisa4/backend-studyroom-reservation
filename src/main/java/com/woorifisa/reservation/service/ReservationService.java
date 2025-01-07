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
        log.info("{}에 존재하는 예약 목록을 조회하는데 성공했습니다.", date);

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
        log.info("사용자({}, {})가 {}에 {}부터 {}까지 회의실 {}을 예약하는데 성공하였습니다.", reserver.getName(), reserver.getEmail(), reservation.getDate(), reservation.getStart(), reservation.getEnd(), reservation.getRoom());

        return new CreateReservationResponseDTO(savedReservation);
    }

    private void validateReservation(Reservation reservation) throws ReservationConflictException {
        List<Reservation> existingReservations = reservationRepository.findByDate(reservation.getDate());

        for (Reservation existingReservation : existingReservations) {
            if (isConflict(existingReservation, reservation)) {
                String message = String.format("이미 예약된 시간대에 예약을 시도하였습니다. (기존 예약: %s ~ %s, 시도 예약: %s ~ %s)", existingReservation.getStart(), existingReservation.getEnd(), reservation.getStart(), reservation.getEnd()) + System.lineSeparator() + String.format("사용자 (%s, %s)가 %s에 %s부터 %s까지 회의실 %s을 예약하는데 실패하였습니다.", reservation.getReserver().getName(), reservation.getReserver().getEmail(), reservation.getDate(), reservation.getStart(), reservation.getEnd(), reservation.getRoom());
                log.warn(message);
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
            String message = String.format("존재하지 않는 예약을 삭제하려고 시도하였습니다. (id: %d)", id) + System.lineSeparator() + String.format("id %d에 해당하는 예약을 삭제하는데 실패하였습니다.", id);
            log.warn(message);
            throw new ReservationNotFoundException("해당 예약을 삭제하는데 실패하였습니다.");
        }

        reservationRepository.deleteById(id);
        log.info("예약 삭제 완료: {}", id);
    }
}
