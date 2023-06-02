package com.damda.back.data.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KaKaoAccessDTO {
    private long id;
    private String connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccountDTO kakaoAccount;
}
