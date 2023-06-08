package com.damda.back.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode {

    RESERVATION_FORM_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"저장 중 에러 발생"),
    KAKAO_TOKEN_EXPIRE(HttpStatus.BAD_REQUEST,"카카오 토큰 만료됨"),
    KAKAO_LOGIN_FALIE(HttpStatus.INTERNAL_SERVER_ERROR,"카카오 로그인중 에러 - 인가코드 또는 액세스 토큰이 잘못됨"),
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST,"조회하려는 유저가 데이터 상에 존재하지 않습니다."),
    NOT_FOUND_QUESTION(HttpStatus.BAD_REQUEST,"없는 데이터를 조회했습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST,"없는 카테고리를 조회했습니다."),
    NOT_FOUND_QUESTION_MODIFIED(HttpStatus.BAD_REQUEST,"없는 데이터를 수정요청했습니다"),
    NO_REQUIRED_VALUE(HttpStatus.BAD_REQUEST,"필수 값 중에 누락된 값이 있습니다"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"DB 커넥션 에러"),
    ERROR_WHILE_SUBMITTING_USER_FORM(HttpStatus.INTERNAL_SERVER_ERROR,"유저 폼 제출 중에 에러가 발생"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"알 수 없는 데이터가 넘어왔습니다.");

    private HttpStatus httpStatus;

    private String message;
}
