package com.damda.back.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAutoResponseDTO {
	private Long reservationId;
	private String name;
	private String address;
	private String reservationDate;
	private List<String> before;
	private List<String> after;
}
