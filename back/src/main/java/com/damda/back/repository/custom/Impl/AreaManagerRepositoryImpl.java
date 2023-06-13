package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.area.QArea;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.QAreaManager;
import com.damda.back.domain.manager.QManager;
import com.damda.back.repository.custom.AreaManagerCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AreaManagerRepositoryImpl implements AreaManagerCustomRepository {


	private final JPAQueryFactory queryFactory;
	@Override
	public List<AreaManager> findAreaManagerList(Long areaId) {

		QAreaManager qAreaManager = QAreaManager.areaManager;
		QManager qManager = QManager.manager;
		QArea qArea = QArea.area;

//		JPAQueryFactory query = queryFactory.selectDistinct(qAreaManager)
//				.from(qAreaManager)
//				.innerJoin(qAreaManager.managerId.manager,qManager).fetchJoin()
//				.innerJoin(qAreaManager.managerId.area,qArea).fetchJoin()
//				.where(qAreaManager.managerId.area.id.eq(areaId))
		return null;
	}
}
