package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.QReservationSubmitForm;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.repository.custom.ReservationFormCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationFormRepositoryImpl implements ReservationFormCustomRepository {

        private final JPAQueryFactory queryFactory;


        public List<ReservationSubmitForm> reservationFormList(){
                QReservationSubmitForm reservationForm = QReservationSubmitForm.reservationSubmitForm;

                return  queryFactory.selectDistinct(reservationForm)
                        .from(reservationForm)
                        .where(reservationForm.deleted.eq(false))
                        .fetch();
        }


}
