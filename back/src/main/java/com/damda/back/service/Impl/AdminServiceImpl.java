package com.damda.back.service.Impl;


import com.damda.back.data.request.AdminLoginRequestDTO;
import com.damda.back.domain.Member;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.MemberRepository;
import com.damda.back.service.AdminService;
import com.damda.back.utils.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;

    private final JwtManager jwtManager;

    private final PasswordEncoder passwordEncoder;
    public String loginProcessing(AdminLoginRequestDTO dto){

         Optional<Member> optional =  memberRepository.findByAdmin(dto.getUsername());

         optional.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));

         Member member = optional.get();

         if(!passwordEncoder.matches(dto.getPassword(),member.getPassword())) throw new CommonException(ErrorCode.ADMIBN_PASSWORD_FAIL);

         String role = member.getRole().name();
         String jwt = jwtManager.jwtTokenAdmin(member.getId(),role);
         return jwt;
    }
}
