package com.damda.back.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode {

    RESERVATION_FORM_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"저장 중 에러 발생");
    private HttpStatus httpStatus;

    private String message;
}
