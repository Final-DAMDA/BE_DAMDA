package com.damda.back.service.Impl;

import com.damda.back.data.common.MemberRole;
import com.damda.back.data.response.MemberResponseDTO;
import com.damda.back.data.response.UserResponseDTO;
import com.damda.back.domain.Member;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.MemberRepository;
import com.damda.back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDTO detail(Integer id) {
        Optional<Member> optional = memberRepository.findByIdWhereActive(id);

        if(optional.isPresent()){
            Member member = optional.get();
            MemberResponseDTO dto = MemberResponseDTO.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .address(member.getAddress())
                    .gender(member.getGender())
                    .phoneNumber(member.getPhoneNumber())
                    .profileImage(member.getProfileImage())
                    .role(member.getRole())
                    .build();

            return dto;
        }else{
            throw new CommonException(ErrorCode.NOT_FOUND_MEMBER);
        }
    }

    @Override
    public boolean existCode(String code) {
        return memberRepository.existCode(code);
    }



    public List<UserResponseDTO> listMember(){
        return memberRepository.findByMemberListWithCode();
    }
}
