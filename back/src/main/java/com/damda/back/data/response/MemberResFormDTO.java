package com.damda.back.data.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResFormDTO {


    private String applicantName;

    private String applicantConactInfo;

    private String address;

    private String aFewServings;

    private String serviceDuraction;

    private String serviceDate;

    private String parkingAvailable;

    private String reservationNote; //매니저가 알아야 할 정보

    private String reservationRequest; // 요청사항

    private String reservationEnter; // 들어가기 위해 필요한 정보

    private String discountCode;

    private String learnedRoute; //알게된 경로


}
