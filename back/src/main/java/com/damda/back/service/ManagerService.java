package com.damda.back.service;

import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import com.damda.back.data.request.ManagerUpdateRequestDTO;
import com.damda.back.data.response.ManagerRegionUpdateResponseDTO;
import com.damda.back.data.response.ManagerResponseDTO;
import com.damda.back.data.response.ManagerUpdateResponseDTO;
import com.damda.back.domain.manager.ManagerStatusEnum;

import java.util.List;

public interface ManagerService {
    
    public boolean managerCreate(ManagerApplicationDTO dto, Integer memberId);

    public List<ManagerResponseDTO> managerResponseDTOList(ManagerStatusEnum managerStatusEnum);
    
    ManagerUpdateResponseDTO managerUpdate(ManagerUpdateRequestDTO dto, Long managerId);

    ManagerRegionUpdateResponseDTO managerRegionUpdate(ManagerRegionUpdateRequestDTO dto, Long managerId);
}
