package com.damda.back.domain.manager;

/**
 * 자격증 여부
 * (Certificate status)
 */
public enum CertificateStatusEnum {
    FIRST_RATE_OFF("1급(오프라인)"),
    SECOND_RATE_OFF("2급(오프라인)"),
    FIRST_RATE_ON("1급(온라인)"),
    SECOND_RATE_ON("2급(온라인)"),
    NONE("없음"),

    ETC("기타");

    private String value;

    CertificateStatusEnum(String value) {
        this.value = value;
    }
}
