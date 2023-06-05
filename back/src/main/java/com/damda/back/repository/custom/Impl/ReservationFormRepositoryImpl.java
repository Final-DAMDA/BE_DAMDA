package com.damda.back.repository.custom.Impl;

import com.amazonaws.services.ec2.model.Reservation;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.response.Statistical;
import com.damda.back.domain.QReservationSubmitForm;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.repository.custom.ReservationFormCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
                                ReservationStatus.PAYMENT_COMPLETED,
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


}
