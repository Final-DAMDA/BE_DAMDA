package com.damda.back.data.response;


import com.damda.back.data.common.PayMentStatus;
import com.damda.back.data.common.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitResponseDTO {

    private String createdAt;

    private String username;

    private String phoneNumber;

    private String address;

    private String reserveDate;

    private String totalPrice;

    private String progressTime;

    @Builder.Default
    private List<String> managers = new ArrayList<>(); //매니저 이름 들어가고 인원은 해당 길이를 체크하는게 좋을듯

    private ReservationStatus reservationStatus;

    private PayMentStatus payMentStatus;




}
