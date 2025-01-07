package com.woorifisa.reservation.controller;

import com.woorifisa.reservation.dto.BaseResponse;
import com.woorifisa.reservation.dto.CreateReservationRequestDTO;
import com.woorifisa.reservation.dto.CreateReservationResponseDTO;
import com.woorifisa.reservation.dto.GetReservationInfoResponseDTO;
import com.woorifisa.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/{date}")
    public ResponseEntity<BaseResponse<List<GetReservationInfoResponseDTO>>> getReservationsByDate(@PathVariable String date) {
        List<GetReservationInfoResponseDTO> data = reservationService.getReservationsByDate(LocalDate.parse(date));
        BaseResponse<List<GetReservationInfoResponseDTO>> response = new BaseResponse<>(HttpStatus.OK.value(), String.format("예약 현황을 조회하는데 성공했습니다.", date), data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse<CreateReservationResponseDTO>> createReservation(@RequestBody CreateReservationRequestDTO body) {
        CreateReservationResponseDTO data = reservationService.createReservation(body);
        BaseResponse<CreateReservationResponseDTO> response = new BaseResponse<>(HttpStatus.CREATED.value(), "예약에 성공했습니다.", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.NO_CONTENT.value(), "예약이 취소되었습니다.", null);

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
