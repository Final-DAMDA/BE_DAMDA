package com.damda.back.data.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenWithImageDTO {

    private String token;
    private String profileImage;

}
