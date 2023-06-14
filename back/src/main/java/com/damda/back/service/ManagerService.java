package com.damda.back.service;

import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.response.ManagerResponseDTO;

import java.util.List;

public interface ManagerService {
    
    public boolean managerCreate(ManagerApplicationDTO dto, Integer memberId);

    public List<ManagerResponseDTO> managerResponseDTOList();
}
