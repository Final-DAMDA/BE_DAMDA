package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.area.Area;
import com.damda.back.domain.area.DistrictEnum;
import com.damda.back.domain.area.QArea;
import com.damda.back.domain.manager.*;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.custom.ManagerCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManagerRepositoryImpl implements ManagerCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Manager> managerList(ManagerStatusEnum managerStatusEnum) {

        QManager manager = QManager.manager;

        List<Manager> list = queryFactory.selectDistinct(manager)
                .from(manager)
                .where(manager.currManagerStatus.eq(managerStatusEnum))
                .orderBy(manager.updatedAt.asc())
                .fetch();

        return list;

    }

    // public List<DistrictEnum> districtEnumList(){
    //     QManager manager = QManager.manager;
    //
    //     return queryFactory.select(manager.activityArea)
    //             .from(manager)
    //             .fetch();
    // }


    /**
     * @ManyToMany fetchJoin OneToMany 관계를 같이 영속화시키면서 ManyToOne 관계의 데이터도 모두 영속화 시킨다.
     * 그렇기 떄문에 칼럼은 모든 테이블의 칼럼이 포함되는 구조이지만, row수는 AreaManager 테이블의 영향만 받게 된다.
     */
    public List<Manager> managerWithArea(String addressFront) {
    //
    //     QManager manager = QManager.manager;
    //     QAreaManager areaManager = QAreaManager.areaManager;
    //     QArea area = QArea.area;
    //
    //
    //     List<Manager> managers = queryFactory
    //             .selectDistinct(manager)
    //             .from(manager)
    //             .join(manager.areaManagers, areaManager).fetchJoin()
    //             .join(areaManager.managerId.area, area).fetchJoin()
    //             .where(area.district.eq(addressFront))
    //             .fetch();
    //
        return null; // TODO: 원래 return managers;
    }


    public List<Manager> managers(List<Long> ids) {
        QManager manager = QManager.manager;

        return queryFactory
                .selectDistinct(manager)
                .from(manager)
                .where(manager.id.in(ids)).fetch();

    }

}
