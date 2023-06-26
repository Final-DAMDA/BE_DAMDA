package com.damda.back.service;

import com.damda.back.data.common.RegionModify;
import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import com.damda.back.data.request.ManagerStatusUpdateRequestDTO;
import com.damda.back.data.request.ManagerUpdateRequestDTO;
import com.damda.back.data.response.ManagerResponseDTO;
import com.damda.back.data.response.PageManagerResponseDTO;
import com.damda.back.domain.manager.ManagerStatusEnum;

import org.springframework.transaction.annotation.Isolation;

import org.springframework.data.domain.Pageable;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ManagerService {
    
    boolean managerCreate(ManagerApplicationDTO dto, Integer memberId);

    @Transactional(readOnly = true)
    ManagerResponseDTO managerResponseDTO(Long managerId);

    public PageManagerResponseDTO managerResponseDTOList(ManagerStatusEnum managerStatusEnum, Pageable pageable);
    
    boolean managerUpdate(ManagerUpdateRequestDTO dto, Long managerId);

    boolean managerRegionUpdate(ManagerRegionUpdateRequestDTO dto, Long managerId);


    void activityRegionADD(Long managerId, Map<RegionModify, String> region);
    void activityRegionDelete(Long managerId, Map<RegionModify, String> region);

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    boolean managerStatusUpdate(ManagerStatusUpdateRequestDTO dto, Long managerId);
}
