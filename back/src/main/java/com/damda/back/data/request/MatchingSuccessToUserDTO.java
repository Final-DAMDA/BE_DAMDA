package com.damda.back.data.request;

import com.damda.back.data.common.QuestionIdentify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingSuccessToUserDTO {
	private String phoneNumber;
	private String reservationDate;
	private String reservationAddress;
	private String reservationHour;
	private Integer managerAmount;
	private Integer totalPrice;

	public MatchingSuccessToUserDTO (Map<QuestionIdentify, String> answerMap, Integer managerAmount, Integer totalPrice){
		this.managerAmount = managerAmount;
		this.phoneNumber = answerMap.get(QuestionIdentify.APPLICANTCONACTINFO);
		this.reservationDate = answerMap.get(QuestionIdentify.SERVICEDATE);
		this.reservationAddress = answerMap.get(QuestionIdentify.ADDRESS);
		this.reservationHour = answerMap.get(QuestionIdentify.SERVICEDURATION);
		this.totalPrice = totalPrice;
	}
}
