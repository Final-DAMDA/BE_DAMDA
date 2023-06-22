package com.damda.back.service;


import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.request.MatchingCompletedDTO;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.manager.Manager;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;

import java.util.List;
import java.util.Map;

public interface TalkSendService {
    public void sendReservationSubmitAfter(Long formId, String addressFront, List<ReservationAnswer> answers, Integer totalPrice, Integer servicePerson, List<Manager> managerList);

    public void sendManagerWithCustomer(MatchingCompletedDTO dto, List<String> phoneNumbers);


    public void sendCustomenrCompleted(String toPhoneNumber,Long formId);

    public void sendCancellation(List<String> r,  Map<QuestionIdentify, String> answerMap,Integer servicePerson) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException;

}
