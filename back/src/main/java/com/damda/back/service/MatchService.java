package com.damda.back.service;

import com.damda.back.data.response.MatchingAcceptGetDTO;
import com.damda.back.domain.ReservationSubmitForm;

public interface MatchService {

	void matchingListUp(ReservationSubmitForm reservationSubmitForm, String district);

	MatchingAcceptGetDTO matchingAcceptInfo(Long reservationId, Integer memberId);
}
