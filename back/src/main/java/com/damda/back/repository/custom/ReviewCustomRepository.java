package com.damda.back.repository.custom;

import com.damda.back.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewCustomRepository {
	boolean existReservation(Long reservationId);
	Optional<Review> serviceCompleteWithImages(Long reservationId);
	List<Review> reviewList();
	Optional<Review> findByReservationId(Long reservationId);
}
