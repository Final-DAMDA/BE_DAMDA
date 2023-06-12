package com.damda.back.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListAdminDTO {
	private Long reviewId;
	private String name;
	private String address;
	private String title;
	private String content;
	private String createAt;
	private boolean best;
}
