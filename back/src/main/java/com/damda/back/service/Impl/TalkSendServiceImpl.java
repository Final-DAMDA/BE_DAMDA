package com.damda.back.service.Impl;

import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.request.CustomerTalkDTO;
import com.damda.back.data.request.MatchingCompletedDTO;
import com.damda.back.data.request.ResCompleteRequestDTO;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.manager.Manager;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.MatchRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.service.TalkSendService;
import com.damda.back.utils.SolapiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TalkSendServiceImpl implements TalkSendService {


    private final SolapiUtils solapiUtils;

    private final ReservationFormRepository reservationFormRepository;

    private final ManagerRepository managerRepository;

    private final MatchRepository matchRepository;




    /**
     * @apiNote 고객이 예약완료시 매니저에게 알람톡을 보낸 후 확인차 고객한테도 알림톡을 보낸다.
     * */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendReservationSubmitAfter(Long formId, String addressFront,List<ReservationAnswer> answers,Integer totalPrice){

        if(answers.isEmpty()) throw new CommonException(ErrorCode.FORM_NOT_FOUND);
        List<Manager> managerList = managerRepository.managerWithArea(addressFront);
        if(managerList.isEmpty()) throw new CommonException(ErrorCode.ACTIVITY_MANAGER_NOT_FOUND);

        Map<QuestionIdentify, String> answerMap =
                answers.stream()
                .collect(
                        Collectors.toMap(
                                ReservationAnswer::getQuestionIdentify,ReservationAnswer::getAnswer));


        ResCompleteRequestDTO resCompleteRequestDTO = ResCompleteRequestDTO.builder()
                .reservationDate(answerMap.get(QuestionIdentify.SERVICEDATE))
                .reservationHour(answerMap.get(QuestionIdentify.SERVICEDATE).substring(11))
                .managerAmount(answerMap.get(QuestionIdentify.AFEWSERVINGS))
                .userAddressCity(answerMap.get(QuestionIdentify.ADDRESS))
                .reservationParking(answerMap.get(QuestionIdentify.PARKINGAVAILABLE))
                .reservationEnter(answerMap.get(QuestionIdentify.RESERVATIONENTER))
                .reservationNote(answerMap.get(QuestionIdentify.RESERVATIONOTE))
                .reservationRequest(answerMap.get(QuestionIdentify.RESERVATIONREQUEST))
                .acceptLink("https://api/damda.store") //TODO: https:를 제외한 도메인 생성해서 너허야ㅕ함
                .build();

        for (Manager manager : managerList) {
            log.info("전송된 번호 {}" , manager.getPhoneNumber());
            solapiUtils.reservationCompletedSendManager(manager.getPhoneNumber(), resCompleteRequestDTO);
        }


        //-- 고객에게 보내는 알림톡 - 서비스일시(reservationTime), 서비스장소(reserveAddress),
        // 매니저 인원(managerAmount), 예상 소요시간(requiredTime), 견적가(estimate)

        CustomerTalkDTO dto = CustomerTalkDTO.builder()
                .reservationTime(answerMap.get(QuestionIdentify.SERVICEDATE))
                .reservationAddress(answerMap.get(QuestionIdentify.ADDRESS))
                .managerAmount(answerMap.get(QuestionIdentify.AFEWSERVINGS))
                .requiredTime(answerMap.get(QuestionIdentify.SERVICEDURATION))
                .estimate(totalPrice.toString())
                .build();

        solapiUtils.reservationCompletedSendCustomer(dto,answerMap.get(QuestionIdentify.APPLICANTCONACTINFO));


    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendManagerWithCustomer(MatchingCompletedDTO dto, List<String> phoneNumbers){

        solapiUtils.managerMatcingCompleted(phoneNumbers,dto);

    }


}
