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
public class RemindTalkToManagerDTO {
	private String reservationHour;
	private String reservationDate;
	private String managerAmount;
	private String reservationAddress;
	private String reservationParking;
	private String reservationEnter;
	private String reservationNote;
	private String reservationRequest;
	private List<String> phoneNumber;

	public RemindTalkToManagerDTO(Map<QuestionIdentify, String> answerMap,List<String> managerPhone, Integer managerAmount){
		this.reservationHour = answerMap.get(QuestionIdentify.SERVICEDURATION);
		this.reservationDate = answerMap.get(QuestionIdentify.SERVICEDATE);
		this.managerAmount = managerAmount.toString();
		this.reservationAddress = answerMap.get(QuestionIdentify.ADDRESS);
		this.reservationParking = answerMap.get(QuestionIdentify.PARKINGAVAILABLE);
		this.reservationEnter = answerMap.get(QuestionIdentify.RESERVATIONENTER);
		this.reservationNote = answerMap.get(QuestionIdentify.RESERVATIONNOTE);
		this.reservationRequest = answerMap.get(QuestionIdentify.RESERVATIONREQUEST);
		this.phoneNumber = managerPhone;
	}
}
