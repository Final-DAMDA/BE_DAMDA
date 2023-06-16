package com.damda.back.service;


import com.damda.back.data.request.MatchingCompletedDTO;
import com.damda.back.domain.ReservationAnswer;

import java.util.List;

public interface TalkSendService {
    public void sendReservationSubmitAfter(Long formId, String addressFront, List<ReservationAnswer> answers,Integer totalPrice,Integer servicePerson);

    public void sendManagerWithCustomer(MatchingCompletedDTO dto, List<String> phoneNumbers);


    public void sendCustomenrCompleted(String toPhoneNumber,Long formId);
    void sendManagerMatchingSuccess(Long reservationId);
}
