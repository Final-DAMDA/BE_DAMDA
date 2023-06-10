package com.damda.back.repository.custom.Impl;

import com.damda.back.data.common.ReservationStatus;
import com.damda.back.domain.*;
import com.damda.back.repository.custom.ReviewCustomRepository;
import com.querydsl.jpa.impl.JPAQuery;
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

	public Optional<Review> serviceCompleteWithImages(Long reservationId) {
		QReview qReview = QReview.review;
		QImage qReviewImage = QImage.image;

		Review review = queryFactory.selectDistinct(qReview)
				.from(qReview)
				.innerJoin(qReview.reviewImage, qReviewImage).fetchJoin()
				.where(qReview.reservationSubmitForm.id.eq(reservationId))
				.fetchOne();

		return Optional.ofNullable(review);
	}

	@Override
	public List<Review> reviewList(){
		QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
		QMember member = QMember.member;
		QReservationAnswer answer = QReservationAnswer.reservationAnswer;
		QReview review = QReview.review;
		QImage image = QImage.image;

		JPAQuery<Review> query1 =
				queryFactory.selectDistinct(review)
						.from(review)
						.innerJoin(review.reservationSubmitForm,submitForm).fetchJoin()
						.innerJoin(review.reviewImage,image).fetchJoin()
						.leftJoin(submitForm.reservationAnswerList,answer)
						.leftJoin(submitForm.member,member)
						.where(review.status.eq(true));

		List<Review> list = query1.fetch();
		return list;
	}

	@Override
	public Optional<Review> findByReservationId(Long reservationId) {
		QReview qReview = QReview.review;

		Review review = queryFactory.selectDistinct(qReview)
				.from(qReview)
				.where(qReview.reservationSubmitForm.id.eq(reservationId))
				.fetchOne();

		return Optional.ofNullable(review);
	}
	@Override
	public Optional<Review> findByBestReview() {
		QReview qReview = QReview.review;

		Review review = queryFactory.selectDistinct(qReview)
				.from(qReview)
				.where(qReview.best.eq(true))
				.fetchOne();

		return Optional.ofNullable(review);
	}

	@Override
	public Optional<Review> findByBestReviewUser() {
		QReservationSubmitForm submitForm = QReservationSubmitForm.reservationSubmitForm;
		QMember member = QMember.member;
		QReservationAnswer answer = QReservationAnswer.reservationAnswer;
		QReview review = QReview.review;
		QImage image = QImage.image;

		Review review1 =
				queryFactory.selectDistinct(review)
						.from(review)
						.innerJoin(review.reservationSubmitForm,submitForm).fetchJoin()
						.innerJoin(review.reviewImage,image).fetchJoin()
						.leftJoin(submitForm.reservationAnswerList,answer)
						.leftJoin(submitForm.member,member)
						.where(review.best.eq(true))
						.fetchOne();

		return Optional.ofNullable(review1);
	}

}
