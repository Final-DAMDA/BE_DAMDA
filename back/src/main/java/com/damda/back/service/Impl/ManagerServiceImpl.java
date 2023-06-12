package com.damda.back.service.Impl;

import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.response.ManagerResponseDTO;
import com.damda.back.domain.Member;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.ActivityDay;
import com.damda.back.domain.manager.Manager;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.AreaRepository;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.MemberRepository;
import com.damda.back.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final MemberRepository memberRepository;

    private final ManagerRepository managerRepository;
    private final AreaRepository areaRepository;


    @Override
    @Transactional
    public boolean managerCreate(ManagerApplicationDTO dto, Integer memberId) {

        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isEmpty()) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "유저를 찾을 수 없습니다.");
        }

        Manager manager = dto.toManagerEntity(member.get());
        ActivityDay activityDay = new ActivityDay();

        activityDay.addManager(manager);
        manager.addActivityDay(activityDay);

        Optional<Area> area = areaRepository.searchArea(dto.getActivityCity().get(1), dto.getActivityDistrict().get(1));
        //        AreaManagerKey areaManagerKey = new AreaManagerKey(area, manager);
        //
        //        // AreaManager 객체 생성 및 AreaManagerKey 설정
        //        AreaManager areaManager = new AreaManager();
        //        areaManager.setManagerId(areaManagerKey);

        return false;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public List<ManagerResponseDTO> managerResponseDTOList() {

        List<ManagerResponseDTO> managerResponseDTOList = new ArrayList<>();

        managerRepository.managerList().forEach(manager -> {

            ManagerResponseDTO dto = ManagerResponseDTO.builder()
                    .id(manager.getId())
                    .managerName(manager.getManagerName())
                    .managerPhoneNumber(manager.getManagerPhoneNumber())
                    .build();

            managerResponseDTOList.add(dto);
            
        });
        
        return managerResponseDTOList;
        
    }
}
