package com.damda.back.data.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
    PAYMENT_COMPLETED("결제완료"),
    MANAGER_MATCHING_COMPLETED("매니저 매칭완료"),
    WAITING_FOR_MANAGER_REQUEST("매니저 매칭 중..");

    private String msg;
}
