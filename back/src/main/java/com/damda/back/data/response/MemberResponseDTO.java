package com.damda.back.data.response;


import com.damda.back.data.common.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {


    private Integer id;

    private String username;


    private String address;

    private String gender;

    private String phoneNumber;

    private String profileImage;

    private MemberRole role;



}
