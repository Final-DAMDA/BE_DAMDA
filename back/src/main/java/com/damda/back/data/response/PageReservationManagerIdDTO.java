package com.damda.back.data.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageReservationManagerIdDTO {
	private List<ReservationListManagerIDDTO> content;


	private boolean first;

	private boolean last;

	private Long total;
}
