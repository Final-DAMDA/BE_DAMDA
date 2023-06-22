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
public class MatchingSuccessToManagerDTO {
	private List<String> phoneNumber;
	private String reservationDate;
	private String reservationAddress;
	private String reservationHour;

	public MatchingSuccessToManagerDTO(Map<QuestionIdentify, String> answerMap,List<String>phoneNumber){
		this.phoneNumber = phoneNumber;
		this.reservationDate = answerMap.get(QuestionIdentify.SERVICEDATE);
		this.reservationAddress = answerMap.get(QuestionIdentify.ADDRESS);
		this.reservationHour = answerMap.get(QuestionIdentify.SERVICEDURATION);
	}
}
