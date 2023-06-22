package com.damda.back.domain.manager;

public enum ManagerStatusEnum {
    WAITING("대기"), ACTIVE("활동중"), PENDING("보류"), INACTIVE("활동불가");
    private String value;

    ManagerStatusEnum(String value) {
        this.value = value;
    }
}
