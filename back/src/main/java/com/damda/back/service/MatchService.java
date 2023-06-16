package com.damda.back.service;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.response.MatchingAcceptGetDTO;
import com.damda.back.data.response.MatchingListDTO;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;

import java.util.List;

public interface MatchService {

	public void matchingListUp(ReservationSubmitForm reservationSubmitForm, List<Manager> managerList);

	MatchingAcceptGetDTO matchingAcceptInfo(Long reservationId, Integer memberId);

	void matchingAccept(Long reservationId, Integer memberId, MatchResponseStatus matchResponseStatus);
	List<MatchingListDTO> matchingList(Long reservationId);
	void matchingOrder(Long reservationId,List<Long> matchIds);
}
