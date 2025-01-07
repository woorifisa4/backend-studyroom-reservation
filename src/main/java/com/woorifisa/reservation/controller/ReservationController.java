package com.woorifisa.reservation.controller;

import com.woorifisa.reservation.dto.BaseResponse;
import com.woorifisa.reservation.dto.CreateReservationRequestDTO;
import com.woorifisa.reservation.dto.CreateReservationResponseDTO;
import com.woorifisa.reservation.dto.GetReservationInfoResponseDTO;
import com.woorifisa.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/{date}")
    public BaseResponse<List<GetReservationInfoResponseDTO>> getReservationsByDate(@PathVariable String date) {
        List<GetReservationInfoResponseDTO> data = reservationService.getReservationsByDate(LocalDate.parse(date));
        return new BaseResponse<>(200, "Success", data);
    }

    @PostMapping
    public BaseResponse<CreateReservationResponseDTO> createReservation(@RequestBody CreateReservationRequestDTO reservationDTO) {
        CreateReservationResponseDTO data = reservationService.createReservation(reservationDTO);
        return new BaseResponse<>(201, "Reservation created successfully", data);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return new BaseResponse<>(200, "Reservation deleted successfully", null);
    }
}
