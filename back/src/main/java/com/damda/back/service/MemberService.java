package com.damda.back.service;

import com.damda.back.data.response.MemberResponseDTO;
import com.damda.back.data.response.UserResponseDTO;

import java.util.List;

public interface MemberService {

    public MemberResponseDTO detail(Integer id);


    public boolean existCode(String code);

    public List<UserResponseDTO> listMember();

}
