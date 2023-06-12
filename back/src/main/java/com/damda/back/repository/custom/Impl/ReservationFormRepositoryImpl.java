package com.damda.back.repository.custom.Impl;

import com.damda.back.data.common.ReservationStatus;
import com.damda.back.domain.*;
import com.damda.back.domain.manager.QManager;
import com.damda.back.repository.custom.ReservationFormCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReservationFormRepositoryImpl implements ReservationFormCustomRepository {

        private final JPAQueryFactory queryFactory;



        public Map<ReservationStatus, Long> statistical(){
                QReservationSubmitForm reservationSubmitForm = QReservationSubmitForm.reservationSubmitForm;

                List<Tuple> tuple = queryFactory
                        .select(
                             reservationSubmitForm.status, reservationSubmitForm.count()
                        )
                        .from(reservationSubmitForm)
                        .where(reservationSubmitForm.status.in(
                                ReservationStatus.WAITING_FOR_ACCEPT_MATCHING,
                                ReservationStatus.WAITING_FOR_MANAGER_REQUEST,
                                ReservationStatus.MANAGER_MATCHING_COMPLETED,
                                ReservationStatus.SERVICE_COMPLETED,
                                ReservationStatus.RESERVATION_CANCELLATION
                        ))
                        .groupBy(reservationSubmitForm.status)
                        .fetch();

                Map<ReservationStatus, Long> countByStatusMap = tuple.stream()
                        .collect(Collectors.toMap(
                                t -> t.get(reservationSubmitForm.status),
                                t -> t.get(reservationSubmitForm.count())
                ));

                return countByStatusMap;
        }

        public Page<ReservationSubmitForm> formPaging(Pageable pageable, Timestamp startDate,Timestamp endDate){
                QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
                QMember member = QMember.member;
                QReservationAnswer answer = QReservationAnswer.reservationAnswer;

                JPAQuery<ReservationSubmitForm> query =
                        queryFactory.selectDistinct(submitForm)
                        .from(submitForm)
                        .innerJoin(submitForm.member, member).fetchJoin()
                        .where(createdAtBetween(startDate,endDate,submitForm))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize());

                List<ReservationSubmitForm> content = query.fetch();

                return PageableExecutionUtils.getPage(content,pageable,() ->
                        queryFactory
                                .selectDistinct(Wildcard.count)
                                .from(submitForm)
                                .fetch()
                                .get(0)
                );
        }

        public List<String> matches(List<Long> ids){
                QReservationSubmitForm reservationSubmitForm = QReservationSubmitForm.reservationSubmitForm;
                QMatch match = QMatch.match;
                QManager manager = QManager.manager;
                QMember member = QMember.member;

                List<String> usernames = queryFactory
                        .select(member.username)
                        .from(reservationSubmitForm)
                        .join(reservationSubmitForm.matches, match)
                        .join(match.manager, manager)
                        .join(manager.member, member)
                        .where(reservationSubmitForm.id.in(ids))
                        .fetch();

                return usernames;

        }

        @Override
        public List<Long> ids(Long id) {
                QMatch match= QMatch.match;
                QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;

                List<Long> ids = queryFactory.select(match.id)
                        .from(submitForm)
                        .join(submitForm.matches,match)
                        .fetch();

                return ids;
        }

        @Override
        public List<ReservationSubmitForm> formList(Timestamp startDate, Timestamp endDate) {
                QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
                QMember member = QMember.member;
                QReservationAnswer answer = QReservationAnswer.reservationAnswer;


                JPAQuery<ReservationSubmitForm> query =
                        queryFactory.selectDistinct(submitForm)
                                .from(submitForm)
                                .innerJoin(submitForm.reservationAnswerList,answer).fetchJoin()
                                .innerJoin(submitForm.member, member).fetchJoin()
                                .where(createdAtBetween(startDate,endDate,submitForm));

                List<ReservationSubmitForm> list = query.fetch();
                return list;
        }


        private BooleanExpression createdAtBetween(Timestamp startDate, Timestamp endDate, QReservationSubmitForm form) {
                if (startDate != null && endDate != null) {
                        return form.createdAt.between(startDate, endDate);
                } else if (startDate != null) {
                        return form.createdAt.goe(startDate);
                } else if (endDate != null) {
                        return form.createdAt.loe(endDate);
                }
                return null;
        }

        @Override
        public Page<ReservationSubmitForm> serviceCompleteList(Pageable pageable){
                QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
                QMember member = QMember.member;
                QReservationAnswer answer = QReservationAnswer.reservationAnswer;

                List<ReservationSubmitForm> list =
                        queryFactory.selectDistinct(submitForm)
                                        .from(submitForm)
                                        .innerJoin(submitForm.reservationAnswerList,answer).fetchJoin()
                                        .innerJoin(submitForm.member, member).fetchJoin()
                                        .where(submitForm.status.eq(ReservationStatus.SERVICE_COMPLETED))
                                        .offset(pageable.getOffset())
                                        .limit(pageable.getPageSize())
                                        .fetch();

                JPAQuery<Long> count = queryFactory.select(submitForm.count())
                        .from(submitForm)
                        .where(submitForm.status.eq(ReservationStatus.SERVICE_COMPLETED));
                
                return PageableExecutionUtils.getPage(list,pageable,count::fetchOne);
        }
        @Override
        public Optional<ReservationSubmitForm> serviceComplete(Long reservationId){
                QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
                QReservationAnswer answer = QReservationAnswer.reservationAnswer;

                ReservationSubmitForm reservationSubmitForm =
                        queryFactory.selectDistinct(submitForm)
                                .from(submitForm)
                                .innerJoin(submitForm.reservationAnswerList,answer).fetchJoin()
                                .where(submitForm.id.eq(reservationId))
                                .fetchOne();

                return Optional.ofNullable(reservationSubmitForm);
        }


}
