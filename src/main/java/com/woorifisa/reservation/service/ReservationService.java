package com.woorifisa.reservation.service;

import com.woorifisa.reservation.dto.CreateReservationRequestDTO;
import com.woorifisa.reservation.dto.CreateReservationResponseDTO;
import com.woorifisa.reservation.dto.GetReservationInfoResponseDTO;
import com.woorifisa.reservation.entity.Reservation;
import com.woorifisa.reservation.entity.ReservationParticipant;
import com.woorifisa.reservation.entity.User;
import com.woorifisa.reservation.exception.ReservationConflictException;
import com.woorifisa.reservation.exception.ReservationNotFoundException;
import com.woorifisa.reservation.exception.InvalidReservationTimeException;
import com.woorifisa.reservation.repository.ReservationParticipantRepository;
import com.woorifisa.reservation.repository.ReservationRepository;
import com.woorifisa.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        // 예약자 조회
        User reserver = userRepository.findById(requestDTO.getReserver())
                .orElseThrow(() -> new ReservationNotFoundException("예약한 사용자를 찾을 수 없습니다."));

        // 참석자 조회
        List<User> participants = userRepository.findAllById(requestDTO.getParticipants());

        // 예약 객체 생성
        Reservation reservation = Reservation.builder()
                .table(requestDTO.getTable())
                .date(requestDTO.getDate())
                .start(requestDTO.getStart())
                .end(requestDTO.getEnd())
                .description(requestDTO.getDescription())
                .reserver(reserver)
                .build();

        // 예약 검증
        validateReservation(reservation);

        // 예약 저장
        Reservation savedReservation = reservationRepository.save(reservation);

        // 예약 - 참가자 테이블 데이터 저장
        participants.forEach(participant -> {
            ReservationParticipant rp = new ReservationParticipant(null, savedReservation, participant);
            reservationParticipantRepository.save(rp);
        });

        // TODO: 예약자, 참석자에게 예약 정보 전송 로직 추가 필요

        log.info("사용자({}, {})가 {}에 {}부터 {}까지 회의실 {}을 예약하는데 성공하였습니다.",
                reserver.getName(), reserver.getEmail(),
                savedReservation.getDate(), savedReservation.getStart(),
                savedReservation.getEnd(), savedReservation.getTable());

        return new CreateReservationResponseDTO(savedReservation, participants);
    }

    private void validateReservation(Reservation reservation) throws ReservationConflictException {
        // 2시간 제한 검증
        if (reservation.getEnd().minusHours(2).isAfter(reservation.getStart())) {
            log.warn("사용자 ({}, {})가 {}의 회의실 {}을 {} ~ {} 까지 예약을 시도하였으나, 2시간 이상 예약하여 예약에 실패하였습니다.",
                    reservation.getReserver().getName(),
                    reservation.getReserver().getEmail(),
                    reservation.getDate(),
                    reservation.getTable(),
                    reservation.getStart(),
                    reservation.getEnd());
            throw new InvalidReservationTimeException("예약은 최대 2시간까지만 가능합니다.");
        }

        List<Reservation> existingReservations = reservationRepository.findByDate(reservation.getDate());

        for (Reservation existingReservation : existingReservations) {
            if (isConflict(existingReservation, reservation)) {
                log.warn("사용자 ({}, {})가 {} ~ {} 까지 예약을 시도하였으나, {} ~ {} 에 이미 예약이 있으므로 예약에 실패하였습니다.", reservation.getReserver().getName(), reservation.getReserver().getEmail(), reservation.getStart(), reservation.getEnd(), existingReservation.getStart(), existingReservation.getEnd());
                throw new ReservationConflictException("해당하는 시간대에 이미 예약이 있습니다.");
            }
        }
    }

    private boolean isConflict(Reservation existing, Reservation newReservation) {
        return existing.getTable().equals(newReservation.getTable()) // 같은 회의실이면서
                && ((existing.getStart().isBefore(newReservation.getEnd()) && existing.getEnd().isAfter(newReservation.getStart()))); // 시간대가 겹치는 경우
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            log.warn("존재하지 않는 예약을 삭제하려고 시도하였습니다. (id: {})", id);
            throw new ReservationNotFoundException("해당 예약을 삭제하는데 실패하였습니다.");
        }

        // TODO: 해당 예약을 생성한 사용자만 삭제할 수 있도록 권한 검사를 추가해야 함.

        reservationRepository.deleteById(id);
        log.info("예약 삭제 완료: {}", id);
    }
}
