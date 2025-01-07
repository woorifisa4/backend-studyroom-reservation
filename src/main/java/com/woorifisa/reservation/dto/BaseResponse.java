package com.woorifisa.reservation.dto;

import lombok.Getter;

@Getter
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public BaseResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
