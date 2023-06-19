package com.damda.back.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemindTalkToUserDTO {
	private String reservationHour; //몇시로 포맷팅..
	private String phoneNumber;
}
