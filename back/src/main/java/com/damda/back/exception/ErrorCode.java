package com.damda.back.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    RESERVATION_CANCEL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"예약취소 상태변경 중 에러발생"),
    GROUPID_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"그룹아이디 삭제 실패"),
    GROUPID_NOT_FOUND(HttpStatus.BAD_REQUEST,"그룹 아이디를 찾지 못함"),
    STATUS_BAN_REQUEST(HttpStatus.BAD_REQUEST, "현재 서비스 완료상태가 아닌데 결제완료처리를 시도함"),
    RESERVATION_FORM_MISSING_VALUE(HttpStatus.BAD_REQUEST, "제출된 데이터 중 누락된 데이터가 있습니다."),
    ADMIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 username에 대항 어드민을 찾지 못함"),

    ADMIBN_PASSWORD_FAIL(HttpStatus.UNAUTHORIZED, "비밀번호가 틀립니다"),
    KAKAO_REQUIRED_VALUES_IS_EMPTY(HttpStatus.UNAUTHORIZED, "카카오에서 필요한 정보 중 한 개 이상을 못 받아옴"),
    NOT_FOUND_RESERIVATION(HttpStatus.BAD_REQUEST, "예약 정보를 찾지 못함"),
    ACTIVITY_MANAGER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 지역에 활동 가능 매니저를 찾지 못함"),
    RESERVATION_FORM_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "저장 중 에러 발생"),
    KAKAO_TOKEN_EXPIRE(HttpStatus.BAD_REQUEST, "카카오 토큰 만료됨"),
    DATE_FORMAT_EXCEPTION(HttpStatus.BAD_REQUEST, "날짜 포맷이 맞지 않습니다."),
    KAKAO_LOGIN_FALIE(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 로그인중 에러 - 인가코드 또는 액세스 토큰이 잘못됨"),
    NOT_FOUND_ACTIVITYDAY(HttpStatus.BAD_REQUEST, "조회하려는 매니저의 활동 요일 정보가 데이터 상에 존재하지 않습니다."),
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "조회하려는 유저가 데이터 상에 존재하지 않습니다."),
    NOT_FOUND_MANAGER(HttpStatus.BAD_REQUEST, "없는 매니저에 대한 수정을 요청하였습니다"),
    NOT_FOUND_LOGIN_MANAGER(HttpStatus.BAD_REQUEST, "로그인한 사용자가 활동중인 매니저가 아닙니다"),

    NOT_FOUND_QUESTION(HttpStatus.BAD_REQUEST,"없는 데이터를 조회했습니다."),
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST,"없는 카테고리를 조회했습니다."),
    NOT_FOUND_QUESTION_MODIFIED(HttpStatus.BAD_REQUEST,"없는 데이터를 수정요청했습니다"),
    NO_REQUIRED_VALUE(HttpStatus.BAD_REQUEST,"필수 값 중에 누락된 값 혹은 중복된 값이 있습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"DB 커넥션 에러"),
    FORM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,"저장되지 않은 폼 값이 넘어왔습닌다."),
    ERROR_WHILE_SUBMITTING_USER_FORM(HttpStatus.INTERNAL_SERVER_ERROR,"유저 폼 제출 중에 에러가 발생"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"알 수 없는 데이터가 넘어왔습니다."),
    SEND_KAKAO_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"카카오톡 전송 실패"),
    ERROR_SERVICE_COMPLETE(HttpStatus.INTERNAL_SERVER_ERROR,"서비스 완료 폼 저장 중에 에러가 발생했습니다. "),
    ERROR_IMAGE_COMPLETE(HttpStatus.INTERNAL_SERVER_ERROR,"이미지 저장 중에 에러가 발생했습니다. "),
    ERROR_REVIEW_COMPLETE(HttpStatus.INTERNAL_SERVER_ERROR,"리뷰 저장 중에 에러가 발생했습니다. "),
    ERROR_BEST_REVIEW_COMPLETE(HttpStatus.INTERNAL_SERVER_ERROR,"리뷰 저장 중에 에러가 발생했습니다. "),
    NOT_FOUND_BEST_REVIEW(HttpStatus.BAD_REQUEST,"지정된 베스트리뷰가 없습니다. "),
    NOT_FOUND_MATCH(HttpStatus.BAD_REQUEST,"해당 매치정보가 없거나 이미 답변했습니다 "),
    NOT_FOUND_MATCH_ID(HttpStatus.BAD_REQUEST,"요청 매치 ID가 비어있습니다. "),
    SUBMITTED_REVIEW_COMPLETE(HttpStatus.BAD_REQUEST,"이미 해당 예약의 리뷰가 존재합니다. "),
    SUBMITTED_SERVICE_COMPLETE(HttpStatus.BAD_REQUEST,"이미 서비스 완료폼이 제출되었습니다. "),
    SUBMITTED_MATCH_ORDER_COMPLETE(HttpStatus.BAD_REQUEST,"이미 어드민이 해당 매칭을 수락하였습니다. "),
    ERROR_MATCH_COMPLETE(HttpStatus.INTERNAL_SERVER_ERROR,"매치 데이터 저장 중에 에러가 발생했습니다."),
    OVER_MATCH_ORDER(HttpStatus.BAD_REQUEST,"매칭 수락된 매니저가 지정된 매니저 수보다 많습니다. "),
    NOT_FOUND_AREA(HttpStatus.BAD_REQUEST,"DB에 해당 지역이 존재하지 않습니다. "),
    ERROR_GROUP_ID_CODE(HttpStatus.INTERNAL_SERVER_ERROR,"리마인드 톡 예약정보 저장 중에 에러가 발생했습니다. "),
    ERROR_COMPLETE_TALK(HttpStatus.INTERNAL_SERVER_ERROR,"서비스 완료 폼 예약톡에 에러가 발생했습니다. "),
    ERROR_REMIND_TALK_USER(HttpStatus.INTERNAL_SERVER_ERROR,"유저 리마인드 예약톡에 에러가 발생했습니다. "),
    ERROR_REMIND_TALK_MANAGER(HttpStatus.INTERNAL_SERVER_ERROR,"매니저 리마인드 예약톡에 에러가 발생했습니다. "),

    NOT_FOUND_QNA(HttpStatus.BAD_REQUEST, "없는 QnA를 조회했습니다."),
    STATUS_BAD_REQUEST(HttpStatus.BAD_REQUEST,"예약취소로 변경할 수 없는 상태값입니다." ),

    NOT_FOUND_SERVICE_COMPLETE(HttpStatus.BAD_REQUEST, "없는 예약이거 서비스 완료 폼이 제출되지 않은 예약입니다. "),
    NOT_FOUND_IMAGE(HttpStatus.BAD_REQUEST, "해당아이디로 조회할 수 없는 이미지입니다.  "),
    EXIST_AREA_MANAGER(HttpStatus.BAD_REQUEST,"이미 해당매니저의 활동지역입니다."),
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST, "해당아이디로 조회할 수 없는 리뷰입니다.  ");




    private HttpStatus httpStatus;

    private String message;
}
