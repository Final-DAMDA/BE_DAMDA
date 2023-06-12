package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.QMatch;
import com.damda.back.domain.QReservationSubmitForm;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.QManager;
import com.damda.back.repository.custom.MatchCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchCustomRepository {


    private final JPAQueryFactory queryFactory;


    public List<Manager> matchingManagerInfo(Long formId){
        QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
        QMatch match = QMatch.match;
        QManager manager = QManager.manager;

        List<Manager> managerList = queryFactory
                .selectDistinct(manager)
                .from(manager)
                .join(submitForm.matches,match).fetchJoin()
                .join(match.manager,manager).fetchJoin()
                .where(match.reservationForm.id.eq(formId))
                .fetch();

        return managerList;
    }



}
