package com.damda.back.utils;


import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoBadRequestException;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Slf4j
public class SolapiUtils {



    @Value("${channal.partner}")
    public String OfPartner;

    private final DefaultMessageService messageService;

    public SolapiUtils(String apiKey,String secretKey){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey,secretKey,"https://api.solapi.com");
    }

    /**
     * @apiNote 매니저가 서비스 예약시 보내는 곳
     * @return SingleMessageSentResponse(groupId = G4V20230609161743TUX3RLAAHYGNO9J, to = 01040783843, from = 01099636287, type = ATA, statusMessage = 정상 접수 ( 이통사로 접수 예정) , country=82, messageId=M4V20230609161743DVEYDZXAL5IAYZD, statusCode=2000, accountId=23052316644799)
     *  NurigoBadRequestException 처리가 필요ㅗ
     * */
    public void reservationCompletedSendManager(){
        KakaoOption kakaoOption = new KakaoOption();

        kakaoOption.setPfId(OfPartner);
        kakaoOption.setTemplateId("KA01TP230607053205027Iilud2IFQfl");

        HashMap<String, String> variables = new HashMap<>();
        variables.put("#{reservationDate}", "2023-06-09 12:00:00");
        variables.put("#{reservationHour}", "12:00:00");
        variables.put("#{managerAmount}", "2");
        variables.put("#{userAddressCity}", "서울특별시");
        variables.put("#{userAddressDistrict}", "강동구");
        variables.put("#{userAddressDetails}", "신비아파트 404동");
        variables.put("#{reservationParking}", "지하 주차장 있습니다.");
        variables.put("#{reservationEnter}", "문 따고 들어오시면 됩니다");
        variables.put("#{reservationNote}","가방은 그대로 두세요!");
        variables.put("#{reservationRequest}", "잘 부탁드립니다.");
        variables.put("#{acceptLink}", "https://api.damda.store");



        kakaoOption.setVariables(variables);

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01099636287");
        message.setTo("01040783843");
        message.setKakaoOptions(kakaoOption);

        //try{
            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
//        }catch (NurigoBadRequestException e){
//            throw new CommonException(ErrorCode.SERVER_ERROR);
//        }

        log.info("알림톡 전송 후 응답 객체 {}",response);
        System.out.println(response);

    }

}
