package com.damda.back.utils;


import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.request.*;

import com.damda.back.data.common.CancellationDTO;
import com.damda.back.data.request.CustomerTalkDTO;
import com.damda.back.data.request.MatchingCompletedDTO;
import com.damda.back.data.request.ResCompleteRequestDTO;
import com.damda.back.domain.GroupIdCode;

import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoBadRequestException;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.*;
import java.time.format.DateTimeFormatter;
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

    public SolapiUtils(String apiKey, String secretKey) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.solapi.com");
    }

    /**
     * @return SingleMessageSentResponse(groupId = G4V20230609161743TUX3RLAAHYGNO9J, to = 01040783843, from = 01099636287, type = ATA, statusMessage = 정상 접수 ( 이통사로 접수 예정) , country=82, messageId=M4V20230609161743DVEYDZXAL5IAYZD, statusCode=2000, accountId=23052316644799)
     * NurigoBadRequestException 처리가 필요ㅗ
     * @apiNote 매니저가 서비스 예약시 보내는 곳
     */
    public void reservationCompletedSendManager(List<String> toPhoneNumber, ResCompleteRequestDTO dto) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {

        ArrayList<Message> messageList = new ArrayList<>();

        toPhoneNumber.forEach(phoneNumber -> {
            KakaoOption kakaoOption = new KakaoOption();

            kakaoOption.setPfId(OfPartner);
            kakaoOption.setTemplateId("KA01TP230610060324504DHhZlkDUtRr"); //템플릿 ID 수정해야함

            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{reservationDate}", dto.getReservationDate());
            variables.put("#{reservationHour}", dto.getReservationHour());
            variables.put("#{managerAmount}", dto.getManagerAmount());
            variables.put("#{userAddress}", dto.getUserAddressCity());   //이거 승인되면 ㅂ녀수 수저
            variables.put("#{reservationParking}", dto.getReservationParking());
            variables.put("#{reservationEnter}", dto.getReservationEnter());
            variables.put("#{reservationNote}", dto.getReservationNote());
            variables.put("#{reservationRequest}", dto.getReservationRequest());
            variables.put("#{domain}", domain + "?id=" + dto.getFormId());


            kakaoOption.setVariables(variables);

            Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
            message.setFrom("01099636287");
            message.setTo(phoneNumber);
            message.setKakaoOptions(kakaoOption);

            messageList.add(message);
        });

        MultipleDetailMessageSentResponse response = this.messageService.send(messageList);

        String str = response.getGroupInfo().getGroupId();

        log.info("알림톡 전송 후 응답 객체 {}", response);
        System.out.println(response);

    }

    /**
     * @return NurigoBadRequestException 처리가 필요ㅗ
     * @apiNote 고객이 예약완료시 고객에게 서비스 내역 보내는 것
     */
    public void reservationCompletedSendCustomer(CustomerTalkDTO dto, String toPhoneNumber) {
        KakaoOption kakaoOption = new KakaoOption();

        kakaoOption.setPfId(mainCh);
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

    public void managerMatcingCompleted(List<String> toPhoneNumber, MatchingCompletedDTO dto) {


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

    public void cancellationSendManager(List<String> toPhoneNumber, CancellationDTO dto) throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        ArrayList<Message> messageList = new ArrayList<>();

        toPhoneNumber.forEach(phoneNumber -> {
            KakaoOption kakaoOption = new KakaoOption();
            kakaoOption.setPfId(OfPartner);
            // 등록하신 카카오 알림톡 템플릿의 templateId를 입력해주세요.
            kakaoOption.setTemplateId("KA01TP230612052312561Brix40rFUIn");

            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{reservationDate}", dto.getReservationDate());
            variables.put("#{reservationHour}", dto.getReservationHour());
            variables.put("#{managerAmount}", dto.getManagerAmount());
            variables.put("#{reservationAddress}", dto.getReservationAddress());
            variables.put("#{reservationParking}", dto.getReservationParking());
            variables.put("#{reservationEnter}", dto.getReservationEnter());
            variables.put("#{reservationNote}", dto.getReservationNote());
            variables.put("#{reservationRequest}", dto.getReservationRequest());

            kakaoOption.setVariables(variables);

            Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.

            message.setFrom("01099636287");
            message.setTo(phoneNumber);
            message.setKakaoOptions(kakaoOption);

            messageList.add(message);
        });

        // try {
        // send 메소드로 단일 Message 객체를 넣어도 동작합니다!
        MultipleDetailMessageSentResponse response = this.messageService.send(messageList);


        // 중복 수신번호를 허용하고 싶으실 경우 위 코드 대신 아래코드로 대체해 사용해보세요!
        //MultipleDetailMessageSentResponse response = this.messageService.send(messageList, true);

        System.out.println(response);
//
//        } catch (NurigoMessageNotReceivedException exception) {
//            System.out.println(exception.getFailedMessageList());
//            System.out.println(exception.getMessage());
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//        }
    }


    /**
     * @apiNote 결제완료시 보내는 카톡
     */
    public void serviceCompletedSendTalk(String toPhoneNumber, Long formId) {

        KakaoOption kakaoOption = new KakaoOption();

        kakaoOption.setPfId(mainCh);
        kakaoOption.setTemplateId("KA01TP2306100607150398Gs50ssTUnD");

        String domainQuery = domain + "?id=" + formId;

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{domain}", domainQuery);

        log.info(domainQuery);
        kakaoOption.setVariables(variables);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01099636287");
        message.setTo(toPhoneNumber);
        message.setKakaoOptions(kakaoOption);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

    }

    public void managerMatchingSuccess(MatchingSuccessToManagerDTO dto) {


        ArrayList<Message> messageList = new ArrayList<>();


        dto.getPhoneNumber().forEach(phoneNumber -> {
            KakaoOption kakaoOption = new KakaoOption();
            kakaoOption.setPfId(OfPartner);
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

            System.out.println("매칭 성공 매니저"+response);

        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * @apiNote : 매칭 실패한 매니저들에게 실패메시지
     */
    public void managerMatchingFail(MatchingFailToManagerDTO dto) {

        ArrayList<Message> messageList = new ArrayList<>();

        dto.getPhoneNumber().forEach(phoneNumber -> {
            KakaoOption kakaoOption = new KakaoOption();
            kakaoOption.setPfId(OfPartner);
            // 등록하신 카카오 알림톡 템플릿의 templateId를 입력해주세요.
            kakaoOption.setTemplateId("KA01TP230612051510650KTBOVkfqHyw");

            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{reservationDate}", dto.getReservationDate());
            variables.put("#{reservationAddress}", dto.getReservationAddress());
            variables.put("#{reservationHour}", dto.getReservationHour());
            variables.put("#{managerAmount}", dto.getManagerAmount().toString());

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

            System.out.println("매칭실패매니저 ==="+response);

        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * @apiNote : 예약 확정시 유저에게 보내는 알림톡
     */
    public void userMatchingSuccess(MatchingSuccessToUserDTO dto) {
        KakaoOption kakaoOption = new KakaoOption();

        kakaoOption.setPfId(mainCh);
        kakaoOption.setTemplateId("KA01TP230612085654132vRJHPdo6507");


        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{reservationDate}", dto.getReservationDate());
        variables.put("#{reservationAddress}", dto.getReservationAddress());
        variables.put("#{managerAmount}", dto.getManagerAmount().toString());
        variables.put("#{reservationHour}", dto.getReservationHour());
        variables.put("#{totalPrice}", dto.getTotalPrice().toString());

        kakaoOption.setVariables(variables);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01099636287");
        message.setTo(dto.getPhoneNumber());
        message.setKakaoOptions(kakaoOption);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println("User전송-----"+response);
    }

    /**
     * @apiNote : 고객 리마인드 알림톡
     */
    public String userRemindTalk(RemindTalkToUserDTO dto) {
        KakaoOption kakaoOption = new KakaoOption();

        kakaoOption.setPfId(mainCh);
        kakaoOption.setTemplateId("KA01TP230607122454569CmujjGt3Sme");


        String reservationHour = dto.getReservationHour();
        LocalDateTime localDateTime = LocalDateTime.parse(reservationHour, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalTime localTime = localDateTime.toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{ReservationHour}", formattedTime);
        //TODO: 시간만 따로 parse 해야함

        kakaoOption.setVariables(variables);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01099636287");
        message.setTo(dto.getPhoneNumber());
        message.setKakaoOptions(kakaoOption);

        try {
            // Java LocalDateTime, Instant 기준, Kolintx의 datetime 내 Instant 타입을 넣어도 동작합니다!
            //LocalDateTime localDateTime = LocalDateTime.parse(dto.getReservationHour(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            localDateTime = localDateTime.minusDays(1); //예약 하루 전
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(localDateTime);
            Instant instant = localDateTime.toInstant(zoneOffset);


            // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
            MultipleDetailMessageSentResponse response = this.messageService.send(message, instant);
            System.out.println("리마인드 톡User전송1-----"+response.getGroupInfo().getGroupId());


        } catch (NurigoMessageNotReceivedException exception) {
            // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        System.out.println("리마인드 톡User전송3-----"+message.getGroupId());
        return message.getGroupId();
    }

    /**
     * @apiNote : 매니저 리마인드 알림톡
     */
    public String managerRemindTalk(RemindTalkToManagerDTO dto) {
        ArrayList<Message> messageList = new ArrayList<>();

        dto.getPhoneNumber().forEach(phoneNumber -> {
            KakaoOption kakaoOption = new KakaoOption();
            kakaoOption.setPfId(OfPartner);
            // 등록하신 카카오 알림톡 템플릿의 templateId를 입력해주세요.
            kakaoOption.setTemplateId("KA01TP230612045854598581lP9pkErD");

            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{reservationDate}",dto.getReservationDate());
            variables.put("#{reservationHour}", dto.getReservationHour());
            variables.put("#{mangerAmount}",dto.getManagerAmount());
            variables.put("#{reservationParking}", dto.getReservationParking());
            variables.put("#{reservationAddress}", dto.getReservationAddress());
            variables.put("#{reservationEnter}", dto.getReservationEnter());
            variables.put("#{reservationNote}", dto.getReservationNote());
            variables.put("#{reservationRequest}", dto.getReservationRequest());

            kakaoOption.setVariables(variables);

            Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.

            message.setFrom("01099636287");
            message.setTo(phoneNumber);
            message.setKakaoOptions(kakaoOption);

            messageList.add(message);
        });

        try {
            // Java LocalDateTime, Instant 기준, Kolintx의 datetime 내 Instant 타입을 넣어도 동작합니다!
            LocalDateTime localDateTime = LocalDateTime.parse(dto.getReservationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            localDateTime = localDateTime.minusDays(1); //예약 하루 전
            System.out.println(localDateTime);
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(localDateTime);
            Instant instant = localDateTime.toInstant(zoneOffset);


            // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
            MultipleDetailMessageSentResponse response = this.messageService.send(messageList, instant);

        } catch (NurigoMessageNotReceivedException exception) {
            // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return messageList.get(0).getGroupId();
    }

    /**
     * @apiNote : 매니저 서비스 완료 폼 알림톡 (비포/애프터)
     */
    public String managerServiceCompleteFormSend(RemindTalkToManagerDTO dto) {
        ArrayList<Message> messageList = new ArrayList<>();

        dto.getPhoneNumber().forEach(phoneNumber -> {
            KakaoOption kakaoOption = new KakaoOption();
            kakaoOption.setPfId(OfPartner);
            // 등록하신 카카오 알림톡 템플릿의 templateId를 입력해주세요.
            kakaoOption.setTemplateId("KA01TP230607122818804PShXbWTb05K");

            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{photoLink}", dto.getReservationHour());


            kakaoOption.setVariables(variables);

            Message message = new Message();
            // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.

            message.setFrom("01099636287");
            message.setTo(phoneNumber);
            message.setKakaoOptions(kakaoOption);

            messageList.add(message);
        });

        try {
            // Java LocalDateTime, Instant 기준, Kolintx의 datetime 내 Instant 타입을 넣어도 동작합니다!
            LocalDateTime localDateTime = LocalDateTime.parse(dto.getReservationHour(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            localDateTime = localDateTime.minusDays(1); //예약 하루 전
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(localDateTime);
            Instant instant = localDateTime.toInstant(zoneOffset);


            // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
            MultipleDetailMessageSentResponse response = this.messageService.send(messageList, instant);


        } catch (NurigoMessageNotReceivedException exception) {
            // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return messageList.get(0).getGroupId();
    }



}


