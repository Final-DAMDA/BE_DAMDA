package com.damda.back.data.common;

import lombok.Getter;

@Getter
public enum QuestionIdentify {

    TITILE("대제목"),
    AFEWSERVINGS("몇인분"),
    SERVICEDURATION("서비스 사용시간"),
    ADDRESS("서비스 주소"),
    SERVICEDATE("서비스날짜와 시간"),
    PARKINGAVAILABLE("주차 가능 여부"),
    APPLICANTNAME("신청인 이름"),
    APPLICANTCONACTINFO("선청인 전화번호"),

    LEARNEDROUTE("알게된 경로"), // --

    RESERVATIONENTER("들어가기 위해 필요한 자료"),
    RESERVATIONOTE("알아야 할 사항"),
    RESERVATIONREQUEST("요청사항"),
    SALEAGENT("판매대행"),

    OPTIONAL("추가적인 자료");

    private String data;

    QuestionIdentify(String data){
        this.data = data;
    }

}

