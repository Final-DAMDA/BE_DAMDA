package com.damda.back.data.request;

import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.Review;
import com.damda.back.domain.ServiceComplete;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCompleteRequestDTO {
	private List<MultipartFile> before;
	private List<MultipartFile> after;

	public Review toEntity(ReservationSubmitForm reservationSubmitForm){
		return Review
				.builder()
				.reservationSubmitForm(reservationSubmitForm)
				.status(false)
				.build();
	}

}
