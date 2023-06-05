package com.damda.back.data.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
    PAYMENT_COMPLETED("결제완료"),
    RESERVATION_CANCELLATION("예약 취소"),
    MANAGER_MATCHING_COMPLETED("매니저 매칭완료 - 서비스 예약 완료"),
    WAITING_FOR_MANAGER_REQUEST("매니저 매칭 한명도 안된 상태"),
    WAITING_FOR_ACCEPT_MATCHING("매니저 매칭 한명 이상 된 상태");
    private String msg;
}
