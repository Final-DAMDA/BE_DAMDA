package com.damda.back.service.Impl;

import com.damda.back.data.common.MatchResponseStatus;

import com.damda.back.data.common.CancellationDTO;

import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.request.*;
import com.damda.back.domain.GroupIdCode;
import com.damda.back.domain.Match;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.manager.Manager;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.GroupIdCodeRepository;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.MatchRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.service.TalkSendService;
import com.damda.back.utils.SolapiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TalkSendServiceImpl implements TalkSendService {
    private final GroupIdCodeRepository groupIdCodeRepository;


    private final SolapiUtils solapiUtils;

    private final ReservationFormRepository reservationFormRepository;

    private final ManagerRepository managerRepository;

    private final MatchRepository matchRepository;




    /**
     * @apiNote 고객이 예약완료시 매니저에게 알람톡을 보낸 후 확인차 고객한테도 알림톡을 보낸다.
     * */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendReservationSubmitAfter(Long formId, String addressFront,List<ReservationAnswer> answers,Integer totalPrice,Integer servicePerson, List<Manager> managerList){

        if(answers.isEmpty()) throw new CommonException(ErrorCode.FORM_NOT_FOUND);

        List<String> phoneNumbers = new ArrayList<>();


        Map<QuestionIdentify, String> answerMap =
                answers.stream()
                .collect(
                        Collectors.toMap(
                                ReservationAnswer::getQuestionIdentify,ReservationAnswer::getAnswer));


        ResCompleteRequestDTO resCompleteRequestDTO = ResCompleteRequestDTO.builder()
                .reservationDate(answerMap.get(QuestionIdentify.SERVICEDATE))
                .reservationHour(answerMap.get(QuestionIdentify.SERVICEDATE).substring(11))
                .managerAmount(servicePerson.toString())
                .userAddressCity(answerMap.get(QuestionIdentify.ADDRESS))
                .reservationParking(answerMap.get(QuestionIdentify.PARKINGAVAILABLE))
                .reservationEnter(answerMap.get(QuestionIdentify.RESERVATIONENTER))
                .reservationNote(answerMap.get(QuestionIdentify.RESERVATIONNOTE))
                .reservationRequest(answerMap.get(QuestionIdentify.RESERVATIONREQUEST))
                .formId(formId.toString())
                .build();

        for (Manager manager : managerList) {
            log.info("전송된 번호 {}" , manager.getPhoneNumber());
            phoneNumbers.add(manager.getPhoneNumber());
        }

        try{
            solapiUtils.reservationCompletedSendManager(phoneNumbers, resCompleteRequestDTO); //TODO 활성화필요
        }catch (NurigoEmptyResponseException e) {
            throw new RuntimeException(e);
        } catch (NurigoUnknownException e) {
            throw new RuntimeException(e);
        } catch (NurigoMessageNotReceivedException e) {
            throw new RuntimeException(e);
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

        log.info("알림톡 발송");
        solapiUtils.reservationCompletedSendCustomer(dto,answerMap.get(QuestionIdentify.APPLICANTCONACTINFO));


    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendManagerWithCustomer(MatchingCompletedDTO dto, List<String> phoneNumbers){

        solapiUtils.managerMatcingCompleted(phoneNumbers,dto);

    }




    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendCustomenrCompleted(String toPhoneNumber,Long formId){

        solapiUtils.serviceCompletedSendTalk(toPhoneNumber,formId);
    }

    @Override
    public void sendManagerMatchingSuccess(Long reservationId) {

    }


    /**
     * @apiNote: 예약확정시 성공매니저, 실패매니저, 유저에게 알림톡 및 리마인드 메시지, 30분전 매니저 서비스 완료 폼 제출 톡 보냄
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendReservationCompleted(List<Match> matches, ReservationSubmitForm reservationSubmitForm) {
        List<ReservationAnswer> answers = reservationSubmitForm.getReservationAnswerList();
        Map<QuestionIdentify, String> answerMap
                = answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

        //매칭 성공 매니저들에게 알림톡 보내기
        List<String> managerPhoneNumbers = matches.stream()
                .filter(match -> match.isMatching())
                .map(match -> match.getManager().getPhoneNumber())
                .collect(Collectors.toList());
        if(!managerPhoneNumbers.isEmpty()){
            MatchingSuccessToManagerDTO matchingSuccessToManagerDTO = new MatchingSuccessToManagerDTO(answerMap,managerPhoneNumbers);
            solapiUtils.managerMatchingSuccess(matchingSuccessToManagerDTO);
        }

        //유저에게 알림톡 보내기
        Integer managerAmount = matches.get(0).getReservationForm().getServicePerson();
        Integer totalPrice = reservationSubmitForm.getTotalPrice();
        MatchingSuccessToUserDTO matchingSuccessToUserDTO = new MatchingSuccessToUserDTO(answerMap,managerAmount,totalPrice);
        solapiUtils.userMatchingSuccess(matchingSuccessToUserDTO);

        //매칭 실패 매니저들에게 알림톡 보내기
        List<String> failPhoneNumbers = matches.stream()
                .filter(match -> match.getMatchStatus()== MatchResponseStatus.YES && !match.isMatching())
                .map(match -> match.getManager().getPhoneNumber())
                .collect(Collectors.toList());
        if(!failPhoneNumbers.isEmpty()){
            MatchingFailToManagerDTO matchingFailToManagerDTO = new MatchingFailToManagerDTO(answerMap,failPhoneNumbers,managerAmount);
            solapiUtils.managerMatchingFail(matchingFailToManagerDTO);
        }

        //리마인드 톡 유저한테 보내기
        RemindTalkToUserDTO remindTalkToUserDTO = new RemindTalkToUserDTO
                (answerMap.get(QuestionIdentify.SERVICEDATE), answerMap.get(QuestionIdentify.APPLICANTCONACTINFO));
        String userRemindGroupId = solapiUtils.userRemindTalk(remindTalkToUserDTO);

        //리마인드 톡 매니저에게 보내기
        RemindTalkToManagerDTO remindTalkToManagerDTO = new RemindTalkToManagerDTO(answerMap,managerPhoneNumbers,managerAmount);
        String managerRemindGroupId = solapiUtils.managerRemindTalk(remindTalkToManagerDTO);

        //서비스 완료 폼 매니저에게 보내기

        Optional<String> highestLevelManagerPhoneNumber = matches.stream()
                .filter(Match::isMatching)
                .map(Match::getManager)
                .max(Comparator.comparingInt(manager -> manager.getLevel()))
                .map(Manager::getPhoneNumber);

        String highestLevelManagerPhoneNumberString = highestLevelManagerPhoneNumber.orElseThrow(()->new CommonException(ErrorCode.ERROR_MATCH_COMPLETE));//TODO

        LocalDateTime localDateTime = LocalDateTime.parse(answerMap.get(QuestionIdentify.SERVICEDATE), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String serviceDuration = (String) answerMap.get(QuestionIdentify.SERVICEDURATION);
        String[] parts = serviceDuration.split("시간");
        String hour = parts[0]; // "3"을 얻습니다

        localDateTime = localDateTime.plusHours(Long.valueOf(hour)); //서비스 완료시간 구하기
        localDateTime = localDateTime.minusMinutes(30); //30분 마이너스

        String completeFormLink = "https://fe-damda.vercel.app/manager/completed"+reservationSubmitForm.getId().toString();

        CompleteFormTalkToManagerDTO completeFormTalkToManagerDTO =CompleteFormTalkToManagerDTO
                .builder()
                .sendTime(localDateTime)
                .link(completeFormLink)
                .phoneNumber(managerPhoneNumbers)
                .build();
        String serviceCompleteGroupId = solapiUtils.managerServiceCompleteFormSend(completeFormTalkToManagerDTO);

        //그룹 Code 객체 저장
        GroupIdCode groupIdCode = GroupIdCode.builder()
                .memberGroupId(userRemindGroupId)
                .managerGroupId(managerRemindGroupId)
                .beforeAfterGroupId(serviceCompleteGroupId)
                .submitForm(reservationSubmitForm)
                .build();
        try{
            groupIdCodeRepository.save(groupIdCode);
        }catch (Exception e){
            throw new CommonException(ErrorCode.ERROR_GROUP_ID_CODE);
        }

    }



    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void sendCancellation(List<String> r,  Map<QuestionIdentify, String> answerMap,Integer servicePerson) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {

        CancellationDTO dto = CancellationDTO.builder()
                .reservationDate(answerMap.get(QuestionIdentify.SERVICEDATE))
                .reservationHour(answerMap.get(QuestionIdentify.SERVICEDATE).substring(11))
                .managerAmount(servicePerson.toString())
                .reservationAddress(answerMap.get(QuestionIdentify.ADDRESS))
                .reservationParking(answerMap.get(QuestionIdentify.PARKINGAVAILABLE))
                .reservationEnter(answerMap.get(QuestionIdentify.RESERVATIONENTER))
                .reservationNote(answerMap.get(QuestionIdentify.RESERVATIONNOTE))
                .reservationRequest(answerMap.get(QuestionIdentify.RESERVATIONREQUEST))
                .build();

        solapiUtils.cancellationSendManager(r,dto);

    }


}
