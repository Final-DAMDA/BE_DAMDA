package com.damda.back.data.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayMentStatus {

    PAYMENT_COMPLETED("결제완료"),
    RESERVE_DEPOSIT_COMPLETED_STATUS("준비금 입금 완료상태"),
    NOT_PAID_FOR_ANYTHING("준비금 아직 안 넣은 상태");

    private String msg;


}
