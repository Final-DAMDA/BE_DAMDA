package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.common.ImageType;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.repository.ReviewRepository;
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

	/**
	 * @apiNote: GET 서비스 완료 폼 제출여부 판단
	 */
	@GetMapping("/service/complete/{id}") //id는 예약 id
	public ResponseEntity<CommonResponse<?>> serviceCompleteCheck(@PathVariable Long id){

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.checkServiceComplete(id).getId())
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);

	}

	/***
	 * @apiNote: 서비스 완료 폼 제출
	 */
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

	/**
	 * @apiNote: 서비스 완료 폼 제출된 고객 리스트 조회 (리뷰 작성 안 되어 있는)
	 */
	@GetMapping("/service/complete/list")
	public ResponseEntity<CommonResponse<?>> serviceCompleteList(){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.listServiceComplete())
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	/***
	 * @apiNote: 리뷰 불러오기(더블클릭하면 선택됨)
	 * @param id
	 * @return
	 */

	@GetMapping("/review/auto/{id}")
	public ResponseEntity<CommonResponse<?>> reviewChoice(@PathVariable Long id){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.selectReviewData(id))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	@PostMapping("/review/auto/{id}")
	public ResponseEntity<CommonResponse<?>> reviewUpload(@PathVariable Long id){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.selectReviewData(id))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}


}
