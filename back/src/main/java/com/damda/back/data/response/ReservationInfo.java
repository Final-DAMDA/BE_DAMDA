package com.damda.back.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInfo {
	private String parkAvailable;
	private String reservationEnter;
	private String reservationNote;
	private String reservationRequest;
}
