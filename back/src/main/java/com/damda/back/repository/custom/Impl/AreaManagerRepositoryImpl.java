package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.area.QArea;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.QAreaManager;
import com.damda.back.domain.manager.QManager;
import com.damda.back.repository.custom.AreaManagerCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
				.where(qAreaManager.areaManagerKey.area.district.eq(district).and(qAreaManager.status.eq(true)));

		List<AreaManager> list=query.fetch();

		return list;
	}
	
	public List<AreaManager> findAreaManagerListByManagerId(Long managerId) {
		
		
		return null; 
		
	}
}
