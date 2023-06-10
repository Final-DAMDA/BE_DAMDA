package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ReviewRequestDTO;
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

	/**
	 * @apiNote: GET 서비스 완료 폼 제출여부 판단
	 */
	@GetMapping("/service/complete/{reservationId}") //id는 예약 id
	public ResponseEntity<CommonResponse<?>> serviceCompleteCheck(@PathVariable Long reservationId){

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.checkServiceComplete(reservationId).getId())
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);

	}

	/***
	 * @apiNote: 서비스 완료 폼 제출
	 */
	@PostMapping("/service/complete/{reservationId}") //id는 예약 id
	public ResponseEntity<CommonResponse<?>> serviceCompleteSave(@PathVariable Long reservationId, @RequestParam(value = "before") List<MultipartFile> before, @RequestParam(value = "after")List<MultipartFile> after){
		ServiceCompleteRequestDTO serviceCompleteRequestDTO=new ServiceCompleteRequestDTO();
		serviceCompleteRequestDTO.setBefore(before);
		serviceCompleteRequestDTO.setAfter(after);

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.uploadServiceComplete(reservationId,serviceCompleteRequestDTO))
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
	 */
	@GetMapping("/review/auto/{reservationId}")
	public ResponseEntity<CommonResponse<?>> reviewChoice(@PathVariable Long reservationId){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.selectReviewData(reservationId))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	/**
	 * @apiNote: 리뷰 업로드
	 * @param reservationId
	 */
	@PostMapping("/review/auto/{reservationId}")
	public ResponseEntity<CommonResponse<?>> reviewUpload(@PathVariable Long reservationId,
														  @RequestParam(value = "before",required = false) List<MultipartFile> before,
														  @RequestParam(value = "after",required = false)List<MultipartFile> after,
														  @RequestParam(value = "title")String title,
														  @RequestParam(value = "content")String content){
		ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO();
		reviewRequestDTO.setBefore(before);
		reviewRequestDTO.setAfter(after);
		reviewRequestDTO.setTitle(title);
		reviewRequestDTO.setContent(content);

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.uploadReview(reservationId,reviewRequestDTO))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	/**
	 * @apiNote: 리뷰 리스트(유저)
	 */
	@GetMapping("/user/review/list")
	public ResponseEntity<CommonResponse<?>> reviewList(){

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.listReview())
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	/**
	 * @apiNote: 베스트 리뷰 저장
	 * @return
	 */
	@PostMapping("/review/best/{reviewId}")
	public ResponseEntity<CommonResponse<?>> bestReviewChoice(@PathVariable Long reviewId){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.selectBestReview(reviewId))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	/**
	 * @apiNote: 베스트 리뷰 불러오기
	 * @return
	 */
	@GetMapping("/review/best")
	public ResponseEntity<CommonResponse<?>> bestReviewGet(){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.findBestReview())
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}



}
