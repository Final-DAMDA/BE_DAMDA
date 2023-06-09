package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.area.DistrictEnum;
import com.damda.back.domain.area.QArea;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.QAreaManager;
import com.damda.back.domain.manager.QManager;
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

    // public List<DistrictEnum> districtEnumList(){
    //     QManager manager = QManager.manager;
    //
    //     return queryFactory.select(manager.activityArea)
    //             .from(manager)
    //             .fetch();
    // }


    public List<Manager> managerWithArea(String addressFront){
        QManager manager = QManager.manager;
        QAreaManager areaManager = QAreaManager.areaManager;
        QArea area = QArea.area;

          List<Manager> managers = queryFactory.selectDistinct(manager)
                .from(manager)
                .leftJoin(manager.areaManagers, areaManager).fetchJoin()
                .leftJoin(areaManager.managerId.area, area).fetchJoin()
                 .where(area.district.eq(addressFront))
                .fetch();
        return managers;
    }

}
