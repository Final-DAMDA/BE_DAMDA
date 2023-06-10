package com.damda.back.data.request;

import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
	private String title;
	private String content;


}
