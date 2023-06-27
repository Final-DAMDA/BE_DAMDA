package com.damda.back.service.Impl;

import com.damda.back.data.common.RegionModify;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.CertificateStatusEnum;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.ManagerStatusEnum;
import com.damda.back.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ManagerServiceImplTest {
	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private AreaManagerRepository areaManagerRepository;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private ManagerServiceImpl managerService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ActivityDayRepository activityDayRepository;
	@BeforeEach
	void init(){
		Manager manager = managerRepository.save(Manager.builder()
				.phoneNumber("01040783843")
				.managerName("김재우")
				.currManagerStatus(ManagerStatusEnum.ACTIVE)
				.certificateStatus(CertificateStatusEnum.ETC)
				.level(1)
				.build());

		Manager manager2 = managerRepository.save(Manager.builder()
				.phoneNumber("01039041094")
				.managerName("고예림")
				.currManagerStatus(ManagerStatusEnum.ACTIVE)
				.certificateStatus(CertificateStatusEnum.ETC)
				.level(1)
				.build());

		Manager manager3 = managerRepository.save(Manager.builder()
				.phoneNumber("01082535890")
				.managerName("김형준")
				.currManagerStatus(ManagerStatusEnum.ACTIVE)
				.certificateStatus(CertificateStatusEnum.ETC)
				.level(1)
				.build());


		Area area1 = Area.builder()
				.city("서울특별시")
				.district("강남구")
				.managerCount(1)
				.build();

		Area area2 = Area.builder()
				.city("서울특별시")
				.district("강북구")
				.managerCount(1)
				.build();

		Area area = Area.builder()
				.city("경기도")
				.district("하남시")
				.managerCount(1)
				.build();

		areaRepository.save(area1);
		areaRepository.save(area2);
		areaRepository.save(area);

		AreaManager.AreaManagerKey key = new AreaManager.AreaManagerKey(area1,manager);
		AreaManager.AreaManagerKey key2 = new AreaManager.AreaManagerKey(area1,manager2);
		AreaManager.AreaManagerKey key3 = new AreaManager.AreaManagerKey(area1,manager3);


		AreaManager areaManager = AreaManager.builder()
				.areaManagerKey(key)
				.build();
		AreaManager areaManager2 = AreaManager.builder()
				.areaManagerKey(key2)
				.build();
		AreaManager areaManager3 = AreaManager.builder()
				.areaManagerKey(key3)
				.build();

		areaManagerRepository.save(areaManager);
		areaManagerRepository.save(areaManager2);
		areaManagerRepository.save(areaManager3);

	}
	@Test
	void activityRegionADD() {
		System.out.println("======");
		Map<RegionModify,String> region = new HashMap<>();
		region.put(RegionModify.GYEONGGI,"하남시");
		managerService.activityRegionADD(2L,region);

	}

//	@Test
//	public void testManagerCreate() {
//		// given
//		ManagerApplicationDTO dto = new ManagerApplicationDTO();
//		Integer memberId = 1;
//		Member member = new Member();
//
//		memberRepository.save(member);
//
//		// when
//		boolean result = managerService.managerCreate(dto, memberId);
//
//		// then
//		assertThat(result).isTrue();
//		assertThat(managerRepository.count()).isEqualTo(1);
//		assertThat(activityDayRepository.count()).isEqualTo(1);
//		assertThat(areaManagerRepository.count()).isEqualTo(dto.getRegion().get("서울특별시").size() + dto.getRegion().get("경기도").size());
//	}
}
