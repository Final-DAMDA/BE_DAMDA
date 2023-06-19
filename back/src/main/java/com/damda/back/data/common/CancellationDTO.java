package com.damda.back.data.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationDTO {

    private String reservationDate;

    private String reservationHour;

    private String managerAmount;

    private String reservationAddress;

    private String reservationParking;

    private String reservationEnter;

    private String reservationNote;

    private String reservationRequest;
}
