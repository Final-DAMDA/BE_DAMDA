package com.damda.back.controller;


import com.damda.back.data.common.ImageType;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("/service/complete/{id}") //id는 예약 id
	public void serviceCompleteSave(@PathVariable Long id, @RequestParam(value = "before") List<MultipartFile> before, @RequestParam(value = "after")List<MultipartFile> after){
		ServiceCompleteRequestDTO serviceCompleteRequestDTO=new ServiceCompleteRequestDTO();
		serviceCompleteRequestDTO.setBefore(before);
		serviceCompleteRequestDTO.setAfter(after);
		reviewService.uploadServiceComplete(id,serviceCompleteRequestDTO);
	}
}
