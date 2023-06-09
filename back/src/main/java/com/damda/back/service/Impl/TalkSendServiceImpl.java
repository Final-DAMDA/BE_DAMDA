package com.damda.back.service.Impl;

import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.utils.SolapiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TalkSendServiceImpl {


    private final SolapiUtils solapiUtils;

    private final ReservationFormRepository reservationFormRepository;

    private final ManagerRepository managerRepository;


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendReservationSubmitAfter(Long formId){
        Optional<ReservationSubmitForm> submitFormOptional = reservationFormRepository.submitFormWithAnswer(formId);

        if(submitFormOptional.isEmpty()) throw new CommonException(ErrorCode.FORM_NOT_FOUND);

        ReservationSubmitForm submitForm = submitFormOptional.get();


    }
}
