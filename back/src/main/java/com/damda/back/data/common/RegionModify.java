package com.damda.back.data.common;

public enum RegionModify {
    SEOUL("서울특별시"),
    GYEONGGI("경기도");

    private final String value;

    private RegionModify(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
