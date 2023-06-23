package com.damda.back.controller;


import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.request.ReviewManualRequestDTO;
import com.damda.back.data.request.ReviewRequestDTO;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.repository.AreaManagerRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;
	private final ReservationFormRepository reservationFormRepository;
	private final AreaManagerRepository areaManagerRepository;

	/**
	 * @apiNote: GET 서비스 완료 폼
	 */
	@GetMapping("/service/complete/{reservationId}") //id는 예약 id
	public ResponseEntity<CommonResponse<?>> serviceCompleteCheck(@PathVariable("reservationId") Long reservationId){

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.checkServiceComplete(reservationId))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);

	}

	/***
	 * @apiNote: 서비스 완료 폼 제출
	 */
	@PostMapping("/service/complete/{reservationId}") //id는 예약 id
	public ResponseEntity<CommonResponse<?>> serviceCompleteSave(@PathVariable("reservationId") Long reservationId, @RequestParam(value = "before") List<MultipartFile> before, @RequestParam(value = "after")List<MultipartFile> after){
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
	public ResponseEntity<CommonResponse<?>> serviceCompleteList(@RequestParam(name = "page", defaultValue = "0") int page,
																 @RequestParam(name = "size", defaultValue = "8") int size){

		Pageable pageable = PageRequest.of(page, size);
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.listServiceComplete(pageable))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	/***
	 * @apiNote: 리뷰 불러오기(더블클릭하면 선택됨)
	 */
	@GetMapping("/review/auto/{reservationId}")
	public ResponseEntity<CommonResponse<?>> reviewChoice(@PathVariable("reservationId") Long reservationId){
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
	 */
	@PostMapping("/review/auto/{reservationId}")
	public ResponseEntity<CommonResponse<?>> reviewUpload(@PathVariable("reservationId") Long reservationId,
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
	 * @apiNote: 리뷰 리스트(어드민)
	 */
	@GetMapping("/admin/review/list")
	public ResponseEntity<CommonResponse<?>> reviewListAdmin(@RequestParam(name = "page", defaultValue = "0") int page,
															 @RequestParam(name = "size", defaultValue = "8") int size){

		Pageable pageable = PageRequest.of(page, size);
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.listReviewAdmin(pageable))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}
	/**
	 * @apiNote: 베스트 리뷰 저장
	 * @return
	 */
	@PutMapping("/review/best/{reviewId}")
	public ResponseEntity<CommonResponse<?>> bestReviewChoice(@PathVariable("reviewId") Long reviewId){
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

	/**
	 * @apiNote: 이미지 삭제
	 */
	@DeleteMapping("/review/image/{imageId}")
	public ResponseEntity<CommonResponse<?>> deleteReviewImage(@PathVariable("imageId")Long imageId){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.deleteReviewImage(imageId))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	/**
	 * @apiNote: 리뷰 삭제
	 */
	@DeleteMapping("/review/{reviewId}")
	public ResponseEntity<CommonResponse<?>> deleteReview(@PathVariable("reviewId")Long reviewId){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.deleteReview(reviewId))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}

	/**
	 * @apiNote: 서비스 완료폼(비포/애프터사진)자세히 보기
	 * @return
	 */
	@GetMapping("/service/complete/detail/{id}")
	public ResponseEntity<CommonResponse<?>> beforeAfterImage(@PathVariable("id")Long reservationId){
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.serviceCompleteDetail(reservationId))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}


	/**
	 * @apiNote: 리뷰 업로드
	 */
	@PostMapping("/review/manual")
	public ResponseEntity<CommonResponse<?>> reviewManual(@RequestParam(value = "before") List<MultipartFile> before,
														  @RequestParam(value = "after")List<MultipartFile> after,
														  @RequestParam(value = "title")String title,
														  @RequestParam(value = "content")String content,
														  @RequestParam(value = "address")String address,
														  @RequestParam(value = "serviceDate")String serviceDate,
														  @RequestParam(value = "name")String name){

		ReviewManualRequestDTO reviewManualRequestDTO = ReviewManualRequestDTO.builder()
														.title(title)
														.address(address)
														.before(before)
														.after(after)
														.content(content)
														.serviceDate(serviceDate)
														.name(name)
														.build();

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(reviewService.manualReviewUpload(reviewManualRequestDTO))
				.build();

		return ResponseEntity
				.status(commonResponse.getStatus())
				.body(commonResponse);
	}


}
