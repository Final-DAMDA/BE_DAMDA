package com.damda.back.utils;

import com.damda.back.data.request.RemindTalkToManagerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SolapiUtilsTest {
	@Autowired
	private SolapiUtils solapiUtils;


	@Test
	void test(){
		List<String>phone=new ArrayList<>();
		phone.add("01039041094");
		RemindTalkToManagerDTO dto=RemindTalkToManagerDTO.builder()
				.phoneNumber(phone)
				.managerAmount("1")
				.reservationDate("2023-06-22 14:25:00")
				.reservationEnter("aaa")
				.reservationAddress("경기도 시흥시")
				.reservationNote("ㅁㅁㅁㅁ")
				.reservationParking("주차 가능")
				.reservationHour("3시간")
				.reservationRequest("Request")
				.build();
		solapiUtils.managerRemindTalk(dto);

	}
}