package com.damda.back.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewManualRequestDTO {
	private String title;
	private String name;
	private String address;
	private String serviceDate;
	private List<MultipartFile> before;
	private List<MultipartFile> after;
	private String content;
}
