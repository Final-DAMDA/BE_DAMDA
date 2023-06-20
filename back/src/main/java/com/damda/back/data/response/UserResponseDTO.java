package com.damda.back.data.response;


import com.damda.back.data.common.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {


    private String name;

    private String phoneNumber;

    private String address;

    private ReservationStatus reservationStatus;

    public String createdAt;

    public String memo;

    public String code;


    public void safeFromNull() {
        if (name == null) {name = "없음";}
        if (phoneNumber == null) {phoneNumber = "없음";}
        if (address == null) {address = "없음";}
        if (reservationStatus == null) {reservationStatus = ReservationStatus.NONE;}
        if (createdAt == null) {createdAt = "없음";}
        if (memo == null) {memo = "없음";}
        if (code == null) {code = "없음";}
    }

}
