package com.woorifisa.reservation.exception;

import com.woorifisa.reservation.dto.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<BaseResponse<Void>> handleReservationConflictException(ReservationConflictException ex) {
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleReservationNotFoundException(ReservationNotFoundException ex) {
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidUserInfoException.class)
    public ResponseEntity<BaseResponse<Void>> handleInvalidUserInfoException(InvalidUserInfoException ex) {
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsEmailException.class)
    public ResponseEntity<BaseResponse<Void>> handleAlreadyExistsEmailException(AlreadyExistsEmailException ex) {
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // 데이터 타입이 맞지 않을 때 발생하는 예외 처리 용도 (날짜 형식이 맞지 않는 경우를 처리하기 위한 목적으로 사용)
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 요청입니다.", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        BaseResponse<Map<String, String>> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 요청입니다.", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class) //
    public ResponseEntity<BaseResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        BaseResponse<String> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
