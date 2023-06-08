package com.damda.back.service;

import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.data.response.ServiceCompleteInfoDTO;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.Review;
import com.damda.back.domain.ServiceComplete;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
	boolean uploadServiceComplete(Long reservationId,ServiceCompleteRequestDTO serviceCompleteRequestDTO);
	void saveImage(Review serviceComplete, List<MultipartFile> before, List<MultipartFile> after);
	ReservationSubmitForm checkServiceComplete(Long reservationId);
	List<ServiceCompleteInfoDTO> listServiceComplete();
}
