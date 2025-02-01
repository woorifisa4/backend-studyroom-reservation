package com.woorifisa.reservation.security;

import com.woorifisa.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationChecker {

    private final ReservationRepository reservationRepository;

    public boolean isReservationOwner(Authentication authentication, Long reservationId) {
        if (authentication == null || !authentication.isAuthenticated()) { // 인증되지 않았거나 인증된 사용자가 아닌 경우
            return false;
        }

        Long userId = extractUserId(authentication);
        if (userId == null) {
            return false;
        }

        return reservationRepository.findById(reservationId)
                .map(reservation -> reservation.getReserver().getId().equals(userId))
                .orElse(false);
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }
}
