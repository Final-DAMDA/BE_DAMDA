package com.damda.back.data.common;

public enum QnACategory {

    PRICE("가격"),
    SERVICE("서비스 관련"),
    ETC("기타");

    private String categoryName;

    QnACategory(String categoryName) {
        this.categoryName = categoryName;
    }

}
