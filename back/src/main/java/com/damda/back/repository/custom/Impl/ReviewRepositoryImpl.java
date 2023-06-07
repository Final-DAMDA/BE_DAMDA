package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.QReview;
import com.damda.back.domain.Review;
import com.damda.back.repository.custom.ReviewCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewCustomRepository {
	private final JPAQueryFactory queryFactory;
	@Override
	public boolean existReservation(Long reservationId){
//		QReview qReview = QReview.review;
//		return queryFactory
//				.from(qReview)
//				.where(qReview.r.id.eq(reservationId))
//				.select(qReview.reservationSubmitForm.id)
//				.fetchFirst()!=null;
		return true;
	}
}
