package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.area.Area;
import com.damda.back.domain.area.DistrictEnum;
import com.damda.back.domain.area.QArea;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.QAreaManager;

import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.ManagerStatusEnum;

import com.damda.back.domain.manager.QManager;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.custom.ManagerCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ManagerRepositoryImpl implements ManagerCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Manager> managerList() {
        
        QManager manager = QManager.manager;

        List<Manager> list = queryFactory.selectDistinct(manager)
                .from(manager)
                .where(manager.currManagerStatus.eq(ManagerStatusEnum.ACTIVE))
                .fetch();

        return list;
        
    }

    @Override
    public String findManagerName(Integer memberId) {
        QManager manager = QManager.manager;

        String managerName = queryFactory.selectDistinct(manager.managerName)
                .from(manager)
                .where(manager.member.id.eq(memberId).and(manager.currManagerStatus.eq(ManagerStatusEnum.ACTIVE)))
                .fetchOne();
        return managerName;
    }

    @Override
    public Optional<Manager> findManager(Integer memberId) {
        QManager manager = QManager.manager;

        Manager manager1 = queryFactory.selectDistinct(manager)
                .from(manager)
                .where(manager.member.id.eq(memberId).and(manager.currManagerStatus.eq(ManagerStatusEnum.ACTIVE)))
                .fetchOne();

        return Optional.ofNullable(manager1);
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
     * */
    public List<Manager> managerWithArea(String addressFront){

        QManager manager = QManager.manager;
        QAreaManager areaManager = QAreaManager.areaManager;
        QArea area = QArea.area;


        List<Manager> managers =  queryFactory
                .selectDistinct(manager)
                .from(manager)
                .join(manager.areaManagers, areaManager).fetchJoin()
                .join(areaManager.areaManagerKey.area, area).fetchJoin()
                .where(area.district.eq(addressFront))
                .fetch();

        return managers;
    }


    public List<Manager> managers(List<Long> ids){
        QManager manager = QManager.manager;

        return queryFactory
                .selectDistinct(manager)
                .from(manager)
                .where(manager.id.in(ids)).fetch();

    }

}
