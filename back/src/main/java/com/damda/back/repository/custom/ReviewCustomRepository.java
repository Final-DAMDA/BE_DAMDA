package com.damda.back.repository.custom;

import com.damda.back.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewCustomRepository {
	boolean existReservation(Long reservationId);
	Optional<Review> serviceCompleteWithImages(Long reservationId);
	Page<Review> reviewList(Pageable pageable);
	List<Review> reviewListUser();
	Optional<Review> findByReservationId(Long reservationId);
	Optional<Review> findByBestReview();
	Optional<Review> findByBestReviewUser();
}
