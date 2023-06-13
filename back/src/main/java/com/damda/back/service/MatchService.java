package com.damda.back.service;

import com.damda.back.domain.ReservationSubmitForm;

public interface MatchService {

	void matchingListUp(ReservationSubmitForm reservationSubmitForm, String district);
}
