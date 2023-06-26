package com.damda.back.service.Impl;

import com.damda.back.data.common.RegionModify;
import com.damda.back.data.request.ManagerApplicationDTO;
import com.damda.back.data.request.ManagerRegionUpdateRequestDTO;
import com.damda.back.data.request.ManagerStatusUpdateRequestDTO;
import com.damda.back.data.request.ManagerUpdateRequestDTO;
import com.damda.back.data.response.ManagerResponseDTO;
import com.damda.back.data.response.PageManagerResponseDTO;
import com.damda.back.domain.Member;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.*;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.*;
import com.damda.back.service.ManagerService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;


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

        // --> ActivityDay를 DTO에 담는 코드
        List<Boolean> activityDayList = new ArrayList<>();

        Optional<ActivityDay> optional = activityDayRepository.findById(manager.getActivityDay().getId());

        if (optional.isPresent()) {
            ActivityDay activityDay = optional.get();

            activityDayList.add(activityDay.isOkMonday());
            activityDayList.add(activityDay.isOkTuesday());
            activityDayList.add(activityDay.isOkWednesday());
            activityDayList.add(activityDay.isOkThursday());
            activityDayList.add(activityDay.isOkFriday());
            activityDayList.add(activityDay.isOkSaturday());
            activityDayList.add(activityDay.isOkSunday());

        } else {
            throw new CommonException(ErrorCode.NOT_FOUND_ACTIVITYDAY);
        }
//        ActivityDay activityDay = manager.getActivityDay();  // 근데 이렇게 하면 안되나요?


        // List<AreaManager> -> Map<String, List<String>> 으로 변환 
        Map<String, List<String>> region = new HashMap<>();
        Optional<List<AreaManager>> optionalAreaManager = Optional.ofNullable(areaManagerRepository.findAreaByManagerId(managerId));

        if (optionalAreaManager.isPresent()) {
            List<AreaManager> areaManagerList = optionalAreaManager.get();
            List<String> districtSeoul = new ArrayList<>();
            List<String> districtGyeonggi = new ArrayList<>();
            for (AreaManager areaManager : areaManagerList) {
                String city = areaManager.getAreaManagerKey().getArea().getCity();
                if (city.equals("서울특별시")) {
                    districtSeoul.add(areaManager.getAreaManagerKey().getArea().getDistrict());
                } else {
                    districtGyeonggi.add(areaManager.getAreaManagerKey().getArea().getDistrict());
                }
            }
            region.put("서울특별시", districtSeoul);
            region.put("경기도", districtGyeonggi);
        } else {
            throw new CommonException(ErrorCode.NOT_FOUND_AREA);
        }

        ManagerResponseDTO dto = ManagerResponseDTO
                .builder()
                .id(manager.getId())
                .managerName(manager.getManagerName())
                .managerPhoneNumber(manager.getPhoneNumber())
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

        dto.setActivityDay(activityDayList);
        dto.setRegion(region);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public PageManagerResponseDTO managerResponseDTOList(ManagerStatusEnum managerStatusEnum, Pageable pageable) {

        List<ManagerResponseDTO> managerResponseDTOList = new ArrayList<>();
        Page<Manager> managerPage = managerRepository.managerList(managerStatusEnum, pageable);

        List<Manager> list = managerPage.getContent();
        List<AreaManager> areaManagerList = list.stream()
                .flatMap(manager -> manager.getAreaManagers().stream())
                .collect(Collectors.toList());

        managerRepository.areaList2(areaManagerList);

        list.forEach(manager -> {
            ManagerResponseDTO dto = ManagerResponseDTO
                    .builder()
                    .id(manager.getId())
                    .managerName(manager.getManagerName())
                    .managerPhoneNumber(manager.getPhoneNumber())
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

            List<Boolean> activityDayList = new ArrayList<>();

            Map<String, List<String>> region = new HashMap<>();
            List<AreaManager> areaManagerListPE = manager.getAreaManagers();


            if (!areaManagerListPE.isEmpty()) {

                List<String> districtSeoul = new ArrayList<>();
                List<String> districtGyeonggi = new ArrayList<>();

                for (AreaManager areaManager : areaManagerListPE) {
                    String city = areaManager.getAreaManagerKey().getArea().getCity();

                    if (city.equals("서울특별시")) {
                        districtSeoul.add(areaManager.getAreaManagerKey().getArea().getDistrict());
                    } else {
                        districtGyeonggi.add(areaManager.getAreaManagerKey().getArea().getDistrict());
                    }
                }
                region.put("서울특별시", districtSeoul);
                region.put("경기도", districtGyeonggi);
            } else {
                throw new CommonException(ErrorCode.NOT_FOUND_AREA);
            }

            dto.setRegion(region);

            managerResponseDTOList.add(dto);

        });

        PageManagerResponseDTO pageManagerResponseDTO = PageManagerResponseDTO.builder()
                .content(managerResponseDTOList)
                .first(managerPage.isFirst())
                .last(managerPage.isLast())
                .total(managerPage.getTotalElements())
                .build();
        return pageManagerResponseDTO;

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
        List<String> entitiesToRemoveSeoul = areaDistricts.stream()  // DB에 원래 있었는데 없어진 것들 삭제해야함
                .filter(entity -> !seoul.contains(entity))
                .collect(Collectors.toList());

        List<String> entitiesToInsertSeoul = seoul.stream()    // DB에 저장해야하는 값들 저장해야함
                .filter(dtoString -> !areaDistricts.contains(dtoString))
                .collect(Collectors.toList());

        for (String str : entitiesToRemoveSeoul) {
            Area area = managerRepository.findByAreaManager(str);
            area.minusCount();

            // AreaManager를 조회해서 삭제하는 로직 +1
        }

        for (String str2 : entitiesToInsertSeoul) {
            Area area = managerRepository.findByAreaManager(str2);
            area.plusCount();
            // AreaManager를 생성하는 로직 - 1
        }

        // 경기도일 경우
        List<String> entitiesToRemoveGyeonggi = areaDistricts.stream()  // DB에 원래 있었는데 없어진 것들 삭제해야함
                .filter(entity -> !gyeonggi.contains(entity))
                .collect(Collectors.toList());

        List<String> entitiesToInsertGyeonggi = gyeonggi.stream()    // DB에 저장해야하는 값들 저장해야함
                .filter(dtoString -> !areaDistricts.contains(dtoString))
                .collect(Collectors.toList());

        for (String str : entitiesToRemoveGyeonggi) {
            Area area = managerRepository.findByAreaManager(str);
            area.minusCount();

            // AreaManager를 조회해서 삭제하는 로직 +1
        }

        for (String str2 : entitiesToInsertGyeonggi) {
            Area area = managerRepository.findByAreaManager(str2);
            area.plusCount();
            // AreaManager를 생성하는 로직 - 1
        }

        return true;
    }


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)

    public void activityRegionADD(Long managerId, Map<RegionModify, String> region) {
        if (region.containsKey(RegionModify.SEOUL)) {

            String district = region.get(RegionModify.SEOUL);
            Manager manager = managerRepository.findById(managerId)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MANAGER));
            Area area = areaRepository.searchArea("서울특별시", district)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_AREA));

            AreaManager.AreaManagerKey key = new AreaManager.AreaManagerKey(area, manager);
            AreaManager areaManager = AreaManager.builder().areaManagerKey(key).build();
            Optional findAM = areaManagerRepository.findById(key);
            if (findAM.isPresent()) {
                throw new CommonException(ErrorCode.EXIST_AREA_MANAGER);
            }
            areaManagerRepository.save(areaManager);
            area.plusCount();

        } else if (region.containsKey(RegionModify.GYEONGGI)) {
            String district = region.get(RegionModify.GYEONGGI);
            Manager manager = managerRepository.findById(managerId)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MANAGER));
            Area area = areaRepository.searchArea("경기도", district)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_AREA));

            AreaManager.AreaManagerKey key = new AreaManager.AreaManagerKey(area, manager);
            AreaManager areaManager = AreaManager.builder().areaManagerKey(key).build();
            Optional findAM = areaManagerRepository.findById(key);
            if (findAM.isPresent()) {
                throw new CommonException(ErrorCode.EXIST_AREA_MANAGER);
            }
            areaManagerRepository.save(areaManager);
            area.plusCount();
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void activityRegionDelete(Long managerId, Map<RegionModify, String> region) {
        if (region.containsKey(RegionModify.SEOUL)) {
            String district = region.get(RegionModify.SEOUL);
            AreaManager areaManager = areaManagerRepository.findAreaByManagerId(managerId, "서울특별시", district)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_AREA_MANAGER));
            areaManager.getAreaManagerKey().getArea().minusCount();
            areaManagerRepository.delete(areaManager);
        } else {
            String district = region.get(RegionModify.GYEONGGI);
            AreaManager areaManager = areaManagerRepository.findAreaByManagerId(managerId, "경기도", district)
                    .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_AREA_MANAGER));
            areaManager.getAreaManagerKey().getArea().minusCount();
            areaManagerRepository.delete(areaManager);
        }

    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean managerStatusUpdate(ManagerStatusUpdateRequestDTO dto, Long managerId) {

        Manager manager = managerRepository.findById(managerId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MANAGER));
                
        if (dto.getCurrManagerStatus() == ManagerStatusEnum.ACTIVE) {
            // area테이블에서 
//            area.plusCount();
        } else {
            // managerCount - 1
        }


        manager.updateManagerStatus(dto);

        return true;
    }


}


