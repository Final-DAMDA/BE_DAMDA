package com.damda.back.service.Impl;

import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.MemberRepository;
import com.damda.back.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    
    private final MemberRepository memberRepository;
    
    private final ManagerRepository managerRepository;
    
    // TODO:
    // - activityDay repository
    
    @Override
    @Transactional
    public boolean managerCreate(ManagerApplicationDTO dto, Integer memberId) {
        
        
        
        return false;
    }
}
