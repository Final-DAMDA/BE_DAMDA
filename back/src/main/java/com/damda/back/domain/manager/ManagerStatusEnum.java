package com.damda.back.domain.manager;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum ManagerStatusEnum {
	WAITING("대기"), ACTIVE("활동중"), PENDING("보류"), INACTIVE("활동불가");
	private String value;
}
