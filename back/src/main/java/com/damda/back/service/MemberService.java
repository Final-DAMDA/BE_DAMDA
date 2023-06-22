package com.damda.back.service;

import com.damda.back.data.request.MemoRequestDTO;
import com.damda.back.data.response.*;

import java.util.List;

public interface MemberService {

    public MemberResponseDTO detail(Integer id);


    public boolean existCode(String code);

    public PageUserResponseDTO listMember(String search, Integer page);

    public PageReservationMemberDTO reservationMemberDTOS(Integer memberId, Integer page);
    public MemberResFormDTO memberResFormDTO(Long formId);


    public void memoModify(MemoRequestDTO memoRequestDTO);

}
