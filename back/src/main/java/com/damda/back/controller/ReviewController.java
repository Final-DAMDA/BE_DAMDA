package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.common.ImageType;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;


	@GetMapping("/service/complete/{id}") //id는 예약 id
	public ResponseEntity<CommonResponse<?>> serviceCompleteRead(@PathVariable Long id){

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.checkServiceComplete(id).getId())
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);

	}
	@PostMapping("/service/complete/{id}") //id는 예약 id
	public ResponseEntity<CommonResponse<?>> serviceCompleteSave(@PathVariable Long id, @RequestParam(value = "before") List<MultipartFile> before, @RequestParam(value = "after")List<MultipartFile> after){
		ServiceCompleteRequestDTO serviceCompleteRequestDTO=new ServiceCompleteRequestDTO();
		serviceCompleteRequestDTO.setBefore(before);
		serviceCompleteRequestDTO.setAfter(after);

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.uploadServiceComplete(id,serviceCompleteRequestDTO))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

}
