package com.damda.back.domain.area;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
public enum DistrictEnum implements AreaEnumExt {

    GANGNAM_GU("1", CityEnum.SEOUL, "강남구"),
    GANGDONG_GU("2", CityEnum.SEOUL, "강동구"),
    GANGBUK_GU("3", CityEnum.SEOUL, "강북구"),
    GANGSEO_GU("4", CityEnum.SEOUL, "강서구"),
    GWANAK_GU("5", CityEnum.SEOUL, "관악구"),
    GWANGJIN_GU("6", CityEnum.SEOUL, "광진구"),
    GURO_GU("7", CityEnum.SEOUL, "구로구"),
    GEUMCHEON_GU("8", CityEnum.SEOUL, "금천구"),
    NOWON_GU("9", CityEnum.SEOUL, "노원구"),
    DOBONG_GU("10", CityEnum.SEOUL, "도봉구"),
    DONGDAEMUN_GU("11", CityEnum.SEOUL, "동대문구"),
    DONGJAK_GU("12", CityEnum.SEOUL, "동작구"),
    MAPO_GU("13", CityEnum.SEOUL, "마포구"),
    SEODAEMUN_GU("14", CityEnum.SEOUL, "서대문구"),
    SEOCHO_GU("15", CityEnum.SEOUL, "서초구"),
    SUNGDONG_GU("16", CityEnum.SEOUL, "성동구"),
    SUNGBUK_GU("17", CityEnum.SEOUL, "성북구"),
    SONGPA_GU("18", CityEnum.SEOUL, "송파구"),
    YANGCHEON_GU("19", CityEnum.SEOUL, "양천구"),
    YEONGDEUNGPO_GU("20", CityEnum.SEOUL, "영등포구"),
    YONGSAN_GU("21", CityEnum.SEOUL, "용산구"),
    EUNPYEONG_GU("22", CityEnum.SEOUL, "은평구"),
    JONGRO_GU("23", CityEnum.SEOUL, "종로구"),
    JUNG_GU("24", CityEnum.SEOUL, "중구"),
    JUNGRANG_GU("25", CityEnum.SEOUL, "중랑구"),
    SUWON_SI("26", CityEnum.GYEONGGI_DO, "수원시"),
    GOYANG_SI("27", CityEnum.GYEONGGI_DO, "고양시"),
    YONGIN_SI("28", CityEnum.GYEONGGI_DO, "용인시"),
    SEONGNAM_SI("29", CityEnum.GYEONGGI_DO, "성남시"),
    BUCHEON_SI("30", CityEnum.GYEONGGI_DO, "부천시"),
    HWASEONG_SI("31", CityEnum.GYEONGGI_DO, "화성시"),
    ANSAN_SI("32", CityEnum.GYEONGGI_DO, "안산시"),
    NAMGYANGJU_SI("33", CityEnum.GYEONGGI_DO, "남양주시"),
    ANYANG_SI("34", CityEnum.GYEONGGI_DO, "안양시"),
    PYEONGTAEK_SI("35", CityEnum.GYEONGGI_DO, "평택시"),
    SIHEUNG_SI("36", CityEnum.GYEONGGI_DO, "시흥시"),
    PAJU_SI("37", CityEnum.GYEONGGI_DO, "파주시"),
    UIJEONGBU_SI("38", CityEnum.GYEONGGI_DO, "의정부시"),
    GIMPO_SI("39", CityEnum.GYEONGGI_DO, "김포시"),
    GWANGJU_SI("40", CityEnum.GYEONGGI_DO, "광주시"),
    GWANGMYEONG_SI("41", CityEnum.GYEONGGI_DO, "광명시"),
    GUNPO_SI("42", CityEnum.GYEONGGI_DO, "군포시"),
    HANAM_SI("43", CityEnum.GYEONGGI_DO, "하남시"),
    OSAN_SI("44", CityEnum.GYEONGGI_DO, "오산시"),
    YANGJU_SI("45", CityEnum.GYEONGGI_DO, "양주시"),
    ICHEON_SI("46", CityEnum.GYEONGGI_DO, "이천시"),
    GURI_SI("47", CityEnum.GYEONGGI_DO, "구리시"),
    ANSEONG_SI("48", CityEnum.GYEONGGI_DO, "안성시"),
    POCHEON_SI("49", CityEnum.GYEONGGI_DO, "포천시"),
    UIWANG_SI("50", CityEnum.GYEONGGI_DO, "의왕시"),
    YANGPYEONG_GUN("51", CityEnum.GYEONGGI_DO, "양평군"),
    YEOJU_SI("52", CityEnum.GYEONGGI_DO, "여주시"),
    DONGDUCHEON_SI("53", CityEnum.GYEONGGI_DO, "동두천시"),
    GAPYEONG_GUN("54", CityEnum.GYEONGGI_DO, "가평군"),
    GWACHEON_SI("55", CityEnum.GYEONGGI_DO, "과천시"),
    YEONCHEON_GUN("56", CityEnum.GYEONGGI_DO, "연천군");


    private final String code;
    private final CityEnum parentCode;
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

    @Override
    public CityEnum getParentCode() {
        return parentCode;
    }

    public static List<DistrictEnum> getEnumByGroup(CityEnum parentCode) {
        return AreaEnumGroup.getEnumByGroup(DistrictEnum.class, parentCode);
    }

    public static List<CityEnum> getParentEnum() {
        return AreaEnumGroup.getParentEnum(CityEnum.class);
    }

}
