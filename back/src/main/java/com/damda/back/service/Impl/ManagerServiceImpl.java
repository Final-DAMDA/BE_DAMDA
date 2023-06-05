package com.damda.back.service.Impl;

import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.domain.Member;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.MemberRepository;
import com.damda.back.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    
    private final MemberRepository memberRepository;
    
    private final ManagerRepository managerRepository;
    
    
    
    @Override
    @Transactional
    public boolean managerCreate(ManagerApplicationDTO dto, Integer memberId) {

        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "유저를 찾을 수 없습니다.");
        }
        
        

        return false;
    }
}
