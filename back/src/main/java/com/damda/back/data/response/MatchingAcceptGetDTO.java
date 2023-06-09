package com.damda.back.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchingAcceptGetDTO {
	private Long id;
	private String manager;

	private ServiceInfo serviceInfo;
	private ReservationInfo reservationInfo;

}
