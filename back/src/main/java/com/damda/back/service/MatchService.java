package com.damda.back.service;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.response.MatchingAcceptGetDTO;
import com.damda.back.data.response.MatchingListDTO;
import com.damda.back.domain.ReservationSubmitForm;

import java.util.List;

public interface MatchService {

	void matchingListUp(ReservationSubmitForm reservationSubmitForm, String district);

	MatchingAcceptGetDTO matchingAcceptInfo(Long reservationId, Integer memberId);

	void matchingAccept(Long reservationId, Integer memberId, MatchResponseStatus matchResponseStatus);
	List<MatchingListDTO> matchingList(Long reservationId);
	void matchingOrder(List<Long> matchIds);
}
