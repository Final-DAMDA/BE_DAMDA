package com.damda.back.data.common;

import lombok.Getter;

@Getter
public enum QuestionIdentify {

    AFEWSERVINGS("몇인분"),
    SERVICEDURATION("서비스 사용시간"),
    ADDRESS("서비스 주소"),
    SERVICEDATE("서비스날짜와 시간"),
    PARKINGAVAILABLE("주차 가능 여부"),
    APPLICANTNAME("신청인 이름"),
    APPLICANTCONACTINFO("선청인 전화번호"),

    LEARNEDROUTE("알게된 경로"),

    REQUIREDGUIDELINES("필수 안내사항"),
    OPTIONAL("추가적인 자료");

    private String data;

    QuestionIdentify(String data){
        this.data = data;
    }

}

