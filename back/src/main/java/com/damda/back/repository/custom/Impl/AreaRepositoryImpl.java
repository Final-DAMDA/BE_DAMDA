package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.area.Area;
import com.damda.back.domain.area.CityEnum;
import com.damda.back.domain.area.DistrictEnum;
import com.damda.back.domain.area.QArea;
import com.damda.back.repository.custom.AreaCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AreaRepositoryImpl implements AreaCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Area> searchArea(String city, String district) {

        QArea area = QArea.area;

        BooleanExpression predicate = area.city.eq(city)
                .and(area.district.eq(district));

        Area result = queryFactory.select(area)
                .from(area)
                .where(predicate)
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
