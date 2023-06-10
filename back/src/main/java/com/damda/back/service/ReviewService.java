package com.damda.back.service;

import com.damda.back.data.request.ReviewRequestDTO;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.data.response.ReviewAutoResponseDTO;
import com.damda.back.data.response.ReviewListUserDTO;
import com.damda.back.data.response.ServiceCompleteInfoDTO;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.Review;
import com.damda.back.domain.ServiceComplete;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
	boolean uploadServiceComplete(Long reservationId,ServiceCompleteRequestDTO serviceCompleteRequestDTO);
	ReservationSubmitForm checkServiceComplete(Long reservationId);
	List<ServiceCompleteInfoDTO> listServiceComplete();
	ReviewAutoResponseDTO selectReviewData(Long reservationId);
	void uploadReview(Long reservationId, ReviewRequestDTO reviewRequestDTO);
	List<ReviewListUserDTO> listReview();
	void selectBestReview(Long reviewId);
	ReviewListUserDTO findBestReview();
}
