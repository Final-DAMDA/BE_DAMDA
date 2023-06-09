package com.damda.back.service;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.common.RegionModify;
import com.damda.back.data.response.MatchingAcceptGetDTO;
import com.damda.back.data.response.MatchingListDTO;
import com.damda.back.data.response.PageReservationManagerIdDTO;
import com.damda.back.data.response.ReservationListManagerIDDTO;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface MatchService {

	public void matchingListUp(ReservationSubmitForm reservationSubmitForm, List<Manager> managerList);
	MatchingAcceptGetDTO matchingAcceptInfo(Long reservationId, Integer memberId);
	void matchingAccept(Long reservationId, Integer memberId, MatchResponseStatus matchResponseStatus);
	List<MatchingListDTO> matchingList(Long reservationId);
	void matchingOrder(Long reservationId,List<Long> matchIds);
	PageReservationManagerIdDTO reservationListManagerDTO(Long managerId, Pageable pageable);


}
