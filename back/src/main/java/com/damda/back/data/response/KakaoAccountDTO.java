package com.damda.back.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoAccountDTO {


    private boolean nameNeedsAgreement;
    private String name;
    private boolean hasPhoneNumber;
    private boolean phoneNumberNeedsAgreement;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private boolean hasGender;
    private boolean genderNeedsAgreement;
    private String gender;


    public void nullCheck(){
        if(this.name == null){this.name = "조회되지않음";}
        if(this.phoneNumber == null){this.phoneNumber = "조회되지않음";}
        if(this.gender == null){this.gender = "조회되지않음";}
    }

}

