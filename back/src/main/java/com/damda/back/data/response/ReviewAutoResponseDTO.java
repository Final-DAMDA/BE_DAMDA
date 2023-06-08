package com.damda.back.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAutoResponseDTO {
	private Long reservationId;
	private String name;
	private String address;
	private String reservationDate;
	private Map<Long, String> before;
	private Map<Long, String> after;
}
