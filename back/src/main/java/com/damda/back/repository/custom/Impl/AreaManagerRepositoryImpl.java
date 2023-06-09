package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.area.Area;
import com.damda.back.domain.area.QArea;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.ManagerStatusEnum;
import com.damda.back.domain.manager.QAreaManager;
import com.damda.back.domain.manager.QManager;
import com.damda.back.repository.custom.AreaManagerCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AreaManagerRepositoryImpl implements AreaManagerCustomRepository {


	private final JPAQueryFactory queryFactory;
	@Override
	public List<AreaManager> findAreaManagerList(String district) {

		QAreaManager qAreaManager = QAreaManager.areaManager;
		QManager qManager = QManager.manager;
		QArea qArea = QArea.area;

		JPAQuery<AreaManager> query = queryFactory.selectDistinct(qAreaManager)
				.from(qAreaManager)
				.innerJoin(qAreaManager.areaManagerKey.manager, qManager).fetchJoin()
				.innerJoin(qAreaManager.areaManagerKey.area, qArea).fetchJoin()
				.where(qAreaManager.areaManagerKey.area.district.eq(district).and(qAreaManager.areaManagerKey.manager.currManagerStatus.eq(ManagerStatusEnum.ACTIVE)));

		List<AreaManager> list=query.fetch();

		return list;
	}
	
	public List<AreaManager> findAreaManagerListByManagerId(Long managerId) {

		QAreaManager qAreaManager = QAreaManager.areaManager;

		JPAQuery<AreaManager> query = queryFactory.selectDistinct(qAreaManager)
				.from(qAreaManager)
				.where(qAreaManager.areaManagerKey.manager.id.eq(managerId));

		List<AreaManager> list = query.fetch();

		return list;
		
	}

	@Override
	public List<AreaManager> findAreaByManagerId(Long managerId) {
		QAreaManager qAreaManager = QAreaManager.areaManager;
		QManager qManager = QManager.manager;
		QArea qArea = QArea.area;

		JPAQuery<AreaManager> query = queryFactory.selectDistinct(qAreaManager)
				.from(qAreaManager)
				.innerJoin(qAreaManager.areaManagerKey.manager, qManager).fetchJoin()
				.innerJoin(qAreaManager.areaManagerKey.area, qArea).fetchJoin()
				.where(qAreaManager.areaManagerKey.manager.id.eq(managerId));

		List<AreaManager> list=query.fetch();
		return list;
	}

	@Override
	public Optional<AreaManager> findAreaByManagerId(Long managerId, String city, String district) {
		QAreaManager qAreaManager = QAreaManager.areaManager;
		QManager qManager = QManager.manager;
		QArea qArea = QArea.area;

		AreaManager areaManager  = queryFactory.selectDistinct(qAreaManager)
				.from(qAreaManager)
				.innerJoin(qAreaManager.areaManagerKey.area, qArea).fetchJoin()
				.where(qAreaManager.areaManagerKey.manager.id.eq(managerId)
						.and(qAreaManager.areaManagerKey.area.district.eq(district))).fetchOne();

		return Optional.ofNullable(areaManager);
	}

	@Override
	public List<AreaManager> findAreaByManagerId2(Long managerId) {
		QAreaManager qAreaManager = QAreaManager.areaManager;
		QArea qArea = QArea.area;

		JPAQuery<AreaManager> query = queryFactory.selectDistinct(qAreaManager)
				.from(qAreaManager)
				.innerJoin(qAreaManager.areaManagerKey.area, qArea).fetchJoin()
				.where(qAreaManager.areaManagerKey.manager.id.eq(managerId));

		List<AreaManager> list=query.fetch();
		return list;
	}

}
