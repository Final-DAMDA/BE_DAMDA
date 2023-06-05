package com.damda.back.service;

import com.damda.back.data.request.ManagerApplicationDTO;

public interface ManagerService {
    
    public boolean managerCreate(ManagerApplicationDTO dto, Integer memberId);
    
}
