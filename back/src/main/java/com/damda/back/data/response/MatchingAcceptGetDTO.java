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
	private Long reservationId;
	private String serviceDate;
	private String servings; //몇인분
	private String serviceAddress;
	private String serviceHours;
	private String managerName;
}
