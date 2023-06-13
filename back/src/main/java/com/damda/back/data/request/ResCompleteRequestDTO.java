package com.damda.back.data.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResCompleteRequestDTO {


    private String reservationDate;
    private String reservationHour;

    private String managerAmount;

    private String userAddressCity;

    private String reservationParking;

    private String reservationEnter;

    private String reservationNote;

    private String reservationRequest;

    private String formId;


}
