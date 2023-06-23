package com.damda.back.service;


import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.request.MatchingCompletedDTO;
import com.damda.back.domain.Match;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;

import java.util.List;
import java.util.Map;

public interface TalkSendService {
    public void sendReservationSubmitAfter(Long formId, String addressFront, List<ReservationAnswer> answers,Integer totalPrice,Integer servicePerson);

    public void sendManagerWithCustomer(MatchingCompletedDTO dto, List<String> phoneNumbers);


    public void sendCustomenrCompleted(String toPhoneNumber,Long formId);

    void sendManagerMatchingSuccess(Long reservationId);
    void sendReservationCompleted(List<Match> matches, ReservationSubmitForm reservationSubmitForm);
    void sendServiceCompletedUser(ReservationSubmitForm reservationSubmitForm);

    public void sendCancellation(List<String> r,  Map<QuestionIdentify, String> answerMap,Integer servicePerson) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException;

}
