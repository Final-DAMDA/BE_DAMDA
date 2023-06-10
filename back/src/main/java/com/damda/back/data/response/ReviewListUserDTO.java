package com.damda.back.data.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListUserDTO {
	private String name;
	private String address;
	private String date;
	private String title;
	private String content;
	private List<String> before;
	private List<String> after;
	private boolean bestReview;



}
