package com.damda.back.repository.custom.Impl;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.domain.*;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.QManager;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.repository.custom.MatchCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchCustomRepository {


    private final JPAQueryFactory queryFactory;
    private final ReservationFormRepository reservationFormRepository;


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
                .join(match.reservationForm,submitForm).fetchJoin()
                .join(match.manager,manager).fetchJoin()
                .where(match.reservationForm.id.eq(reservationId))
                .fetch();

        return match1;
    }
    @Override
    public List<String> matchListFindManager(Long reservationId) {
        QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
        QMatch match = QMatch.match;
        QManager manager = QManager.manager;

        List<String> match1 = queryFactory
                .selectDistinct(match.managerName)
                .from(match)
                .where(match.reservationForm.id.eq(reservationId).and(match.matching.eq(true)))
                .fetch();

        return match1;
    }

    public List<Match> matches(Long formId){
        QMatch match = QMatch.match;
        QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
        QManager manager = QManager.manager;

        List<Match> matches = queryFactory.selectDistinct(match)
                .from(match)
                .innerJoin(match.manager,manager).fetchJoin()
                .where(match.reservationForm.id.eq(formId)).fetch();

        return matches;
    }

    @Override
    public Optional<Match> matchFindByReservationAndMatch(Long reservationId, Long matchId) {
        QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
        QMatch match = QMatch.match;

        Match match1 = queryFactory
                .selectDistinct(match)
                .from(match)
                .join(match.reservationForm,submitForm).fetchJoin()
                .where(match.reservationForm.id.eq(reservationId).and(match.id.eq(matchId)).and(match.matchStatus.eq(MatchResponseStatus.YES)))
                .fetchOne();

        return Optional.ofNullable(match1);
    }

    @Override
    public Page<ReservationSubmitForm> findByManagerId(Long managerId, Pageable pageable) {
        QMatch match = QMatch.match;
        QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;

        List<Match> list = queryFactory.selectDistinct(match)
                .from(match)
                .innerJoin(match.reservationForm, submitForm).fetchJoin()
                .where(match.matching.eq(true).and(match.managerId.eq(managerId)))
                .fetch();

        JPAQuery<Long> count = queryFactory.selectDistinct(match.reservationForm.count())
                .from(match)
                .where(match.matching.eq(true).and(match.managerId.eq(managerId)));


        List<ReservationSubmitForm> reservationSubmitForms=list.stream().map(Match::getReservationForm).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(reservationSubmitForms,pageable,count::fetchOne);
    }


}
