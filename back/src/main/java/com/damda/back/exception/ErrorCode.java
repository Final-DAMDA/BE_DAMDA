package com.damda.back.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode {

    RESERVATION_FORM_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"저장 중 에러 발생"),

    NOT_FOUND_QUESTION(HttpStatus.BAD_REQUEST,"없는 데이터를 조회했습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST,"없는 카테고리를 조회했습니다."),
    NOT_FOUND_QUESTION_MODIFIED(HttpStatus.BAD_REQUEST,"없는 데이터를 수정요청했습니다"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST,"알 수 없는 데이터가 넘어왔습니다.");
    private HttpStatus httpStatus;

    private String message;
}
