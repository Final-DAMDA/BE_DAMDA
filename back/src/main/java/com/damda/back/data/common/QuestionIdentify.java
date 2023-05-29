package com.damda.back.data.common;

import lombok.Getter;

@Getter
public enum QuestionIdentify {

    SERVICEDATE("서비스날짜"),
    SERVICELOCATION("서비스지역"),
    SERVICEDURATION("서비스 사용시간"),
    ESTIMATEDCOST("추정 금액"),
    OPTIONAL("추가적인 자료");

    private String data;

    QuestionIdentify(String data){
        this.data = data;
    }

}

