package com.woorifisa.reservation.exception;

import com.woorifisa.reservation.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 지원하지 않는 HTTP 메소드로 요청했을 때 발생하는 예외 처리 용도
    public ResponseEntity<BaseResponse<Void>> handleHttpRequestMethodNotSupportedException() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String requestUri = attributes.getRequest().getRequestURI(); // 요청 URL
        String httpMethod = attributes.getRequest().getMethod(); // HTTP 메소드

        log.warn("지원하지 않는 HTTP 메소드에 요청이 들어왔습니다. / 요청 정보: {} {}", httpMethod, requestUri);

        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.METHOD_NOT_ALLOWED.value(), "올바르지 않은 요청입니다.", null);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // 데이터 타입이 맞지 않을 때 발생하는 예외 처리 용도 (날짜 형식이 맞지 않는 경우를 처리하기 위한 목적으로 사용)
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentTypeMismatchException() {
        BaseResponse<Void> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 요청입니다.", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // @Valid로 @RequestBody에 매핑되는 DTO 클래스를 검증할 때 던져지는 예외 처리 용도
    public ResponseEntity<BaseResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        StringBuilder logMessage = new StringBuilder();

        // FieldError를 통해 각 필드의 에러 메시지 및 값 처리
        for (int i = 0; i < ex.getBindingResult().getFieldErrors().size(); i++) {
            FieldError fieldError = ex.getBindingResult().getFieldErrors().get(i);
            String fieldName = fieldError.getField(); // 필드명
            String errorMessage = fieldError.getDefaultMessage(); // 에러 메시지
            String fieldValue = (String) fieldError.getRejectedValue(); // 필드의 실제 값 가져오기

            errors.put(fieldName, errorMessage);

            // 마지막 항목이 아니면 콤마 추가
            logMessage.append(String.format("%s: \"%s\"", fieldName, fieldValue));
            if (i < ex.getBindingResult().getFieldErrors().size() - 1) {
                logMessage.append(", ");
            }
        }

        // 요청 정보 로깅
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String requestUri = attributes.getRequest().getRequestURI(); // 요청 URL
        String httpMethod = attributes.getRequest().getMethod(); // HTTP 메소드

        String logMsg = String.format("API에 잘못된 요청이 들어왔습니다. / 오류 메시지: %s / 요청 정보: %s %s {%s}",
                String.join(" & ", errors.values()), httpMethod, requestUri, logMessage);

        log.warn(logMsg);

        // 응답
        BaseResponse<Map<String, String>> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 요청입니다.", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
