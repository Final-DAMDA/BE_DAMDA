package com.damda.back.utils;


import com.damda.back.data.request.CustomerTalkDTO;
import com.damda.back.data.request.MatchingCompletedDTO;
import com.damda.back.data.request.ResCompleteRequestDTO;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoBadRequestException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class SolapiUtils {



    @Value("${channal.partner}")
    private String OfPartner;

    @Value("${channal.main}")
    private String mainCh;

    @Value("${damda.domain}")
    private String domain;

    private final DefaultMessageService messageService;

    public SolapiUtils(String apiKey,String secretKey){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey,secretKey,"https://api.solapi.com");
    }

    /**
     * @apiNote 매니저가 서비스 예약시 보내는 곳
     * @return SingleMessageSentResponse(groupId = G4V20230609161743TUX3RLAAHYGNO9J, to = 01040783843, from = 01099636287, type = ATA, statusMessage = 정상 접수 ( 이통사로 접수 예정) , country=82, messageId=M4V20230609161743DVEYDZXAL5IAYZD, statusCode=2000, accountId=23052316644799)
     *  NurigoBadRequestException 처리가 필요ㅗ
     * */
    public void reservationCompletedSendManager(String toPhoneNumber, ResCompleteRequestDTO dto){
        KakaoOption kakaoOption = new KakaoOption();

        kakaoOption.setPfId(OfPartner);
        kakaoOption.setTemplateId("KA01TP230607053205027Iilud2IFQfl"); //템플릿 ID 수정해야함

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{reservationDate}", dto.getReservationDate());
        variables.put("#{reservationHour}", dto.getReservationHour());
        variables.put("#{managerAmount}", dto.getManagerAmount());
        variables.put("#{userAddress}", dto.getUserAddressCity());   //이거 승인되면 ㅂ녀수 수저
        variables.put("#{reservationParking}", dto.getReservationParking());
        variables.put("#{reservationEnter}", dto.getReservationEnter());
        variables.put("#{reservationNote}",dto.getReservationNote());
        variables.put("#{reservationRequest}", dto.getReservationRequest());
        variables.put("#{acceptLink}", dto.getAcceptLink());



        kakaoOption.setVariables(variables);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01099636287");
        message.setTo(toPhoneNumber);
        message.setKakaoOptions(kakaoOption);

        //try{
            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
//        }catch (NurigoBadRequestException e){
//            throw new CommonException(ErrorCode.SERVER_ERROR);
//        }

        log.info("알림톡 전송 후 응답 객체 {}",response);
        System.out.println(response);

    }

    /**
     * @apiNote 고객이 예약완료시 고객에게 서비스 내역 보내는 것
     * @return
     *  NurigoBadRequestException 처리가 필요ㅗ
     * */
    public void reservationCompletedSendCustomer(CustomerTalkDTO dto,String toPhoneNumber){
        KakaoOption kakaoOption = new KakaoOption();

        kakaoOption.setPfId(OfPartner);
        kakaoOption.setTemplateId("KA01TP230607055039283yWlI9RuSJBA");


        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{reservationTime}", dto.getReservationTime());
        variables.put("#{reserveAddress}", dto.getReservationAddress());
        variables.put("#{managerAmount}", dto.getManagerAmount());
        variables.put("#{requiredTime}", dto.getRequiredTime());
        variables.put("#{estimate}", dto.getEstimate());

        kakaoOption.setVariables(variables);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01099636287");
        message.setTo(toPhoneNumber);
        message.setKakaoOptions(kakaoOption);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public void managerMatcingCompleted(List<String> toPhoneNumber, MatchingCompletedDTO dto){


        ArrayList<Message> messageList = new ArrayList<>();


        toPhoneNumber.forEach(phoneNumber -> {
            KakaoOption kakaoOption = new KakaoOption();
            kakaoOption.setPfId(mainCh);
            // 등록하신 카카오 알림톡 템플릿의 templateId를 입력해주세요.
            kakaoOption.setTemplateId("KA01TP230612051326414qMmEGl08iVx");

            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{reservationDate}", dto.getReservationDate());
            variables.put("#{reservationAddress}", dto.getReservationAddress());
            variables.put("#{reservationHour}", dto.getReservationHour());

            kakaoOption.setVariables(variables);

            Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.

            message.setFrom("01099636287");
            message.setTo(phoneNumber);
            message.setKakaoOptions(kakaoOption);

            messageList.add(message);
        });

        try {
            // send 메소드로 단일 Message 객체를 넣어도 동작합니다!
            MultipleDetailMessageSentResponse response = this.messageService.send(messageList);

            // 중복 수신번호를 허용하고 싶으실 경우 위 코드 대신 아래코드로 대체해 사용해보세요!
            //MultipleDetailMessageSentResponse response = this.messageService.send(messageList, true);

            System.out.println(response);

        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void serviceCompletedSendTalk(String toPhoneNumber,Long formId){

        KakaoOption kakaoOption = new KakaoOption();

        kakaoOption.setPfId(mainCh);
        kakaoOption.setTemplateId("KA01TP2306100607150398Gs50ssTUnD"); //템플릿 ID 수정해야함

        String domainQuery = domain + "?id="+formId;

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{domain}" , domainQuery);

        log.info(domainQuery);
        kakaoOption.setVariables(variables);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01099636287");
        message.setTo(toPhoneNumber);
        message.setKakaoOptions(kakaoOption);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

    }

}
