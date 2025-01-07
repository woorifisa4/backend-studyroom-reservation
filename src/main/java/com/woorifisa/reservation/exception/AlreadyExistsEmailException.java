package com.woorifisa.reservation.exception;

public class AlreadyExistsEmailException extends RuntimeException {
    public AlreadyExistsEmailException(String message) {
        super(message);
    }
}
