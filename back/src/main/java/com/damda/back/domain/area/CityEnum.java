package com.damda.back.domain.area;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
public enum CityEnum implements AreaEnum {
    
    SEOUL("1", "서울특별시"),
    GYEONGGI_DO("2", "경기도");
    
    private final String code;
    private final String value;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
    
}
