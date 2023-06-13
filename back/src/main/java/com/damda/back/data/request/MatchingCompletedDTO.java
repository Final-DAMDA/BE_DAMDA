package com.damda.back.data.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchingCompletedDTO {

    private String reservationDate;

    private String reservationAddress;

    private String reservationHour;

}
