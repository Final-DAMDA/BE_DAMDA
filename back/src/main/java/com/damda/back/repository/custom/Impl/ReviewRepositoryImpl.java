package com.damda.back.repository.custom.Impl;

import com.damda.back.domain.QImage;
import com.damda.back.domain.QReview;
import com.damda.back.domain.Review;
import com.damda.back.repository.custom.ReviewCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewCustomRepository {
	private final JPAQueryFactory queryFactory;
	@Override
	public boolean existReservation(Long reservationId){
		QReview qReview = QReview.review;
		return queryFactory
				.from(qReview)
				.where(qReview.reservationSubmitForm.id.eq(reservationId))
				.select(qReview.reservationSubmitForm.id)
				.fetchFirst()!=null;
	}

	public Optional<Review> getReviewWithImages(Long reservationId) {
		QReview qReview = QReview.review;
		QImage qReviewImage = QImage.image;

		Review review = queryFactory.selectDistinct(qReview)
				.from(qReview)
				.leftJoin(qReview.reviewImage, qReviewImage)
				.fetchJoin()
				.where(qReview.reservationSubmitForm.id.eq(reservationId))
				.fetchOne();

		return Optional.ofNullable(review);
	}
}
