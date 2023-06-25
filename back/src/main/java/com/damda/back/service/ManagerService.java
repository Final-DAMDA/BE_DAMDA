package com.damda.back.service;

import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import com.damda.back.data.request.ManagerUpdateRequestDTO;
import com.damda.back.data.response.ManagerResponseDTO;
import com.damda.back.data.response.PageManagerResponseDTO;
import com.damda.back.domain.manager.ManagerStatusEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ManagerService {
    
    boolean managerCreate(ManagerApplicationDTO dto, Integer memberId);

    @Transactional(readOnly = true)
    ManagerResponseDTO managerResponseDTO(Long managerId);

    public PageManagerResponseDTO managerResponseDTOList(ManagerStatusEnum managerStatusEnum, Pageable pageable);
    
    boolean managerUpdate(ManagerUpdateRequestDTO dto, Long managerId);

    boolean managerRegionUpdate(ManagerRegionUpdateRequestDTO dto, Long managerId);
}
