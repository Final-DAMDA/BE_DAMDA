package com.damda.back.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemindTalkToManagerDTO {
	private String reservationHour;
	private String managerAmount;
	private String reservationAddress;
	private String reservationParking;
	private String reservationEnter;
	private String reservationNote;
	private String reservationRequest;
}
