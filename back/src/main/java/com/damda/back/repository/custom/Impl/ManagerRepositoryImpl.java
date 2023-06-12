package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.ManagerStatusEnum;
import com.damda.back.domain.manager.QManager;
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
    public List<Manager> managerList() {
        
        QManager manager = QManager.manager;

        List<Manager> list = queryFactory.selectDistinct(manager)
                .from(manager)
                .where(manager.currManagerStatus.eq(ManagerStatusEnum.ACTIVE))
                // .orderBy(manager.updateAt.asc())
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
}
