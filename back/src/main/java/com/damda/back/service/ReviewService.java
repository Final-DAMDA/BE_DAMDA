package com.damda.back.service;

import com.damda.back.data.request.ReviewRequestDTO;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.data.response.*;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.Review;
import com.damda.back.domain.ServiceComplete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
	boolean uploadServiceComplete(Long reservationId,ServiceCompleteRequestDTO serviceCompleteRequestDTO);
	ServiceCompleteResponseDTO checkServiceComplete(Long reservationId);
	Page<ServiceCompleteInfoDTO> listServiceComplete(Pageable pageable);
	ReviewAutoResponseDTO selectReviewData(Long reservationId);
	boolean uploadReview(Long reservationId, ReviewRequestDTO reviewRequestDTO);
	List<ReviewListUserDTO> listReview();
	boolean selectBestReview(Long reviewId);
	ReviewListUserDTO findBestReview();
	boolean deleteReviewImage(Long imageId);
	boolean deleteReview(Long reviewId);
	Page<ReviewListAdminDTO> listReviewAdmin(Pageable pageable);
}
