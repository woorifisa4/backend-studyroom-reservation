package com.woorifisa.reservation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class BaseResponse<T> {
    private int statusCode;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public BaseResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
