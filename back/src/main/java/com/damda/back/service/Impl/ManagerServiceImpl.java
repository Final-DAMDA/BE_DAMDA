package com.damda.back.service.Impl;

import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.response.ManagerResponseDTO;
import com.damda.back.domain.Member;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.ActivityDay;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.ManagerStatusEnum;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.*;
import com.damda.back.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ActivityDayRepository activityDayRepository;
    private final AreaManagerRepository areaManagerRepository;
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;
    private final AreaRepository areaRepository;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean managerCreate(ManagerApplicationDTO dto, Integer memberId) {

        Optional<Member> member = memberRepository.findById(memberId);

        if(member.isEmpty()) {
        }

        Manager manager = dto.toManagerEntity(member.get());
        ActivityDay activityDay = dto.toDayEntity();
        manager.addActivityDay(activityDay);
        try{
            managerRepository.save(manager);
            activityDayRepository.save(activityDay);
        }catch (Exception e){
            //TODO:
        }

        List<Area> areas = IntStream.range(0,dto.getActivityDistrict().size())
                .mapToObj(i->{
                    String city = dto.getActivityCity().get(i);
                    String district = dto.getActivityDistrict().get(i);
                    Optional<Area> area=areaRepository.searchArea(city,district);
                    System.out.println("---------"+area);
                    if(area.isEmpty()){
                        throw new CommonException(ErrorCode.BAD_REQUEST);
                    }
                    return area.get();
                }).collect(Collectors.toList());


        for(Area a : areas){
            AreaManager areaManager = AreaManager.builder()
                    .managerId(new AreaManager.AreaManagerKey(a,manager))
                    .build();
            try{
                areaManagerRepository.save(areaManager);
            }catch (Exception e){
                //TODO:
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerResponseDTO> managerResponseDTOList(ManagerStatusEnum managerStatusEnum) {

        List<ManagerResponseDTO> managerResponseDTOList = new ArrayList<>();

//        managerRepository.managerList().forEach(manager -> {
//
//            ManagerResponseDTO dto = ManagerResponseDTO.builder()
//                    .id(manager.getId())
//                    .managerName(manager.getManagerName())
//                    .managerPhoneNumber(manager.getManagerPhoneNumber())
//                    .address(manager.getMember().getAddress())
//                    .level(manager.getLevel())
//                    .certificateStatus(manager.getCertificateStatus())
//                    .certificateStatusEtc(manager.getCertificateStatusEtc())
//                    .vehicle(manager.getVehicle())
//                    .prevManagerStatus(manager.getPrevManagerStatus())
//                    .currManagerStatus(manager.getCurrManagerStatus())
//                    .build();
//
//            List<AreaManager> managers = manager.getAreaManagers();
//
//            managerResponseDTOList.add(dto);
//
//        });

        return null;


    }

}


