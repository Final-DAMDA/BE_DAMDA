package com.damda.back.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCompleteTalkUserDTO {
	private String reservationPrice;
	private String managerAmount;
	private String reservationHour;
	private String phoneNumber;


}
