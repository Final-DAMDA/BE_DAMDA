package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.Member;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.area.DistrictEnum;
import com.damda.back.domain.area.QArea;
import com.damda.back.domain.manager.*;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.custom.ManagerCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ManagerRepositoryImpl implements ManagerCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Manager> managerList(ManagerStatusEnum managerStatusEnum, Pageable pageable) {

        QManager manager = QManager.manager;

        BooleanExpression expression = managerStatusEnum != null ? manager.currManagerStatus.eq(managerStatusEnum) : null;


        List<Manager> list = queryFactory.selectDistinct(manager)
                .from(manager)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(manager.updatedAt.asc())
                .fetch();

        return PageableExecutionUtils.getPage(list,pageable,() ->
            queryFactory.select(Wildcard.count)
                    .from(manager)
                    .fetch()
                    .get(0)
        );

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

    @Override
    public Optional<Manager> findMangerWithAreaManger(Long managerId) {
        QManager manager = QManager.manager;
        QAreaManager areaManager = QAreaManager.areaManager;
        QArea area = QArea.area;

        Manager manager1 =  queryFactory.select(manager)
                .from(manager)
                .innerJoin(manager.areaManagers,areaManager).fetchJoin()
                .innerJoin(areaManager.areaManagerKey.area,area).fetchJoin()
                .where(manager.id.eq(managerId))
                .fetchOne();

        if(manager1 != null) return Optional.of(manager1);
        else return Optional.empty();
    }

    public Area findByAreaManager(String dist){
        QArea area = QArea.area;

        return queryFactory.select(area)
                .from(area)
                .where(area.district.eq(dist)).fetchOne();

    }

    @Override
    public List<AreaManager> areaList(List<AreaManager.AreaManagerKey> areaManagers) {
        QManager manager = QManager.manager;
        QAreaManager areaManager = QAreaManager.areaManager;
        QArea area = QArea.area;

        List<AreaManager> areaManagerList = queryFactory.selectDistinct(areaManager)
                .from(areaManager)
                .innerJoin(areaManager.areaManagerKey.area,area).fetchJoin()
                .where(areaManager.areaManagerKey.in(areaManagers)).fetch();
        return areaManagerList;
    }

    public List<AreaManager> areaList2(List<AreaManager> areaManagers) {
        QManager manager = QManager.manager;
        QAreaManager areaManager = QAreaManager.areaManager;
        QArea area = QArea.area;

        List<AreaManager> areaManagerList = queryFactory.selectDistinct(areaManager)
                .from(areaManager)
                .innerJoin(areaManager.areaManagerKey.area,area).fetchJoin()
                .where(areaManager.in(areaManagers)).fetch();
        return areaManagerList;
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

        QManager manager = QManager.manager;
        QAreaManager areaManager = QAreaManager.areaManager;
        QArea area = QArea.area;


        List<Manager> managers = queryFactory
                .selectDistinct(manager)
                .from(manager)
                .join(manager.areaManagers, areaManager).fetchJoin()
                .join(areaManager.areaManagerKey.area, area).fetchJoin()
                .where(area.district.eq(addressFront))
                .where(manager.currManagerStatus.eq(ManagerStatusEnum.ACTIVE))
                .fetch();

        return managers;

    }


    public List<Manager> managers(List<Long> ids) {
        QManager manager = QManager.manager;

        return queryFactory
                .selectDistinct(manager)
                .from(manager)
                .where(manager.id.in(ids)).fetch();

    }

}
