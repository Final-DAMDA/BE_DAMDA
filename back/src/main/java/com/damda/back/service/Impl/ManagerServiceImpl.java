package com.damda.back.service.Impl;

import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import com.damda.back.data.request.ManagerUpdateRequestDTO;
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

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MEMBER));
        Manager manager = dto.toManagerEntity(member);
        ActivityDay activityDay = dto.toDayEntity();
        manager.addActivityDay(activityDay);
        try {
            managerRepository.save(manager);
            activityDayRepository.save(activityDay);
        } catch (Exception e) {
            // TODO:
        }

        for (String district : dto.getRegion().get("서울특별시")) {
            Area area2 = areaRepository.searchArea("서울특별시", district).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_AREA));

            AreaManager areaManager = AreaManager.builder()
                    .areaManagerKey(new AreaManager.AreaManagerKey(area2, manager))
                    .build();

            areaManagerRepository.save(areaManager);
        }

        for (String district : dto.getRegion().get("경기도")) {
            Area area2 = areaRepository.searchArea("경기도", district).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_AREA));

            AreaManager areaManager = AreaManager.builder()
                    .areaManagerKey(new AreaManager.AreaManagerKey(area2, manager))
                    .build();

            areaManagerRepository.save(areaManager);
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerResponseDTO managerResponseDTO(Long managerId) {

        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MANAGER));

        System.out.println(manager);
        
        ManagerResponseDTO dto = ManagerResponseDTO
                .builder()
                .id(manager.getId())
                .managerName(manager.getManagerName())
                .managerPhoneNumber(manager.getManagerPhoneNumber())
                .level(manager.getLevel())
                .certificateStatus(String.valueOf(manager.getCertificateStatus()))
                .certificateStatusEtc(manager.getCertificateStatusEtc())
                .vehicle(manager.getVehicle())
                .fieldExperience(manager.getFieldExperience())
                .mainJobStatus(manager.getMainJobStatus())
                .mainJobStatusEtc(manager.getMainJobStatusEtc())
                .memo(manager.getMemo())
                .prevManagerStatus(String.valueOf(manager.getPrevManagerStatus()))
                .currManagerStatus(String.valueOf(manager.getCurrManagerStatus()))
                .build();

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerResponseDTO> managerResponseDTOList(ManagerStatusEnum managerStatusEnum) {

        List<ManagerResponseDTO> managerResponseDTOList = new ArrayList<>();

        managerRepository.managerList(managerStatusEnum).forEach(manager -> {

            ManagerResponseDTO dto = ManagerResponseDTO
                    .builder()
                    .id(manager.getId())
                    .managerName(manager.getManagerName())
                    .managerPhoneNumber(manager.getManagerPhoneNumber())
                    .level(manager.getLevel())
                    .certificateStatus(String.valueOf(manager.getCertificateStatus()))
                    .certificateStatusEtc(manager.getCertificateStatusEtc())
                    .vehicle(manager.getVehicle())
                    .fieldExperience(manager.getFieldExperience())
                    .mainJobStatus(manager.getMainJobStatus())
                    .mainJobStatusEtc(manager.getMainJobStatusEtc())
                    .memo(manager.getMemo())
                    .prevManagerStatus(String.valueOf(manager.getPrevManagerStatus()))
                    .currManagerStatus(String.valueOf(manager.getCurrManagerStatus()))
                    .build();

            managerResponseDTOList.add(dto);

        });

        return managerResponseDTOList;

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean managerUpdate(ManagerUpdateRequestDTO dto, Long managerId) {

        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MANAGER));

        manager.updateManager(dto);

        return true;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean managerRegionUpdate(ManagerRegionUpdateRequestDTO dto, Long managerId) {

        Manager manager = managerRepository.findMangerWithAreaManger(managerId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MANAGER));

        List<AreaManager> areaManagerList = manager.getAreaManagers();
        List<Area> areaList = areaManagerList.stream().map(areaManager ->
                        areaManager.getAreaManagerKey().getArea())
                .collect(Collectors.toList());


        List<String> areaDistricts = areaList.stream().map(Area::getDistrict).collect(Collectors.toList());
        Map<String, List<String>> map = dto.getCity();

        List<String> seoul = map.get("서울특별시");
        List<String> gyeonggi = map.get("경기도");

        // 서울특별시일 경우
        List<String> entitiesToRemove = areaDistricts.stream()  // DB에 원래 있었는데 없어진 것들 삭제해야함
                .filter(entity -> !seoul.contains(entity))
                .collect(Collectors.toList());

        List<String> entitiesToInsert = seoul.stream()    // DB에 저장해야하는 값들 저장해야함
                .filter(dtoString -> !areaDistricts.contains(dtoString))
                .collect(Collectors.toList());

        for (String str : entitiesToRemove) {
            Area area = managerRepository.findByAreaManager(str);
            area.minusCount();

            // AreaManager를 조회해서 삭제하는 로직 +1
        }

        for (String str2 : entitiesToInsert) {
            Area area = managerRepository.findByAreaManager(str2);
            area.plusCount();
            // AreaManager를 생성하는 로직 - 1
        }

        // 경기도일 경우


        return true;
    }

}


