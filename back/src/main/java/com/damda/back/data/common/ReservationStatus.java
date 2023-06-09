package com.damda.back.data.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
    SERVICE_COMPLETED("서비스 완료"),
    RESERVATION_CANCELLATION("예약 취소"),
    MANAGER_MATCHING_COMPLETED("매니저 매칭완료 - 서비스 예약 확정"),
    WAITING_FOR_MANAGER_REQUEST("매니저 매칭 한명도 안된 상태 - 매칭중...."),
    WAITING_FOR_ACCEPT_MATCHING("매니저 매칭 한명 이상 된 상태 - 매칭수락대기"),
    REVIEW_DATA("리뷰데이터를 위해 수동추가한 데이터"),
    NONE("예약이 없는 상태");

    private String msg;
}
