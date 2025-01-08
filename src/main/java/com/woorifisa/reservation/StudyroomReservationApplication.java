package com.woorifisa.reservation;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class StudyroomReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyroomReservationApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")); // 타임존 설정
    }

}
