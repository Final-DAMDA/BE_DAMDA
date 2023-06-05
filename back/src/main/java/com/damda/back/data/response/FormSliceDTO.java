package com.damda.back.data.response;


import com.damda.back.data.common.PayMentStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.domain.Member;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.fileslice.FileSlice;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FormSliceDTO {

       // , 신청일자, 사용자 이름, 연락처, 주소, 예약일자, 가격 ,소요시간, 매니저 인원
     //   *  ,매니저 매칭(누가지원했는지), 서비스 상태(ReservationStatus),  결제상태

    private String createdAt;

    private String name;

    private String phoneNumber;

    private String address;

    private Integer totalPrice;

    private String estimate;

    private String manageAmount;

    private List<String> managerNames;

    private ReservationStatus reservationStatus;

    private PayMentStatus payMentStatus;



}
