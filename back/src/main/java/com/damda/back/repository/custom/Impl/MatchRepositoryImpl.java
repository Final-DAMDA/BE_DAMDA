package com.damda.back.repository.custom.Impl;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.domain.Match;
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
import java.util.Optional;

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

    @Override
    public Optional<Match> matchFindByReservationAndMember(Long reservationId, Long managerId) {
        QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
        QMatch match = QMatch.match;

        Match match1 = queryFactory
                .selectDistinct(match)
                .from(match)
                .join(match.reservationForm,submitForm).fetchJoin()
                .where(match.reservationForm.id.eq(reservationId).and(match.managerId.eq(managerId)).and(match.matchStatus.eq(MatchResponseStatus.WAITING)))
                .fetchOne();

        return Optional.ofNullable(match1);
    }

    @Override
    public List<Match> matchList(Long reservationId) {
        QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
        QMatch match = QMatch.match;
        QManager manager = QManager.manager;

        List<Match> match1 = queryFactory
                .selectDistinct(match)
                .from(match)
                .innerJoin(match.reservationForm,submitForm).fetchJoin()
                .innerJoin(match.manager,manager).fetchJoin()
                .where(match.reservationForm.id.eq(reservationId))
                .fetch();

        return match1;
    }


}
