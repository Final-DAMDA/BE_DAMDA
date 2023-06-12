package com.damda.back.service.Impl;

import com.damda.back.data.common.ImageType;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.request.ReviewRequestDTO;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.data.response.*;
import com.damda.back.domain.*;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.ImageRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.repository.ReviewRepository;
import com.damda.back.service.ReviewService;
import com.damda.back.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ImageRepository imageRepository;
	private final S3Service s3Service;
	private final ReservationFormRepository reservationFormRepository;
	private final ReviewRepository reviewRepository;


	/**
	 * @apiNote: 서비스 완료 폼 업로드
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public boolean uploadServiceComplete(Long reservationId, ServiceCompleteRequestDTO serviceCompleteRequestDTO){
		Optional<ReservationSubmitForm> reservation = reservationFormRepository.serviceComplete(reservationId);
		nullCheck(reservation);
		ReservationSubmitForm reservationSubmitForm =reservation.get();
		reservationSubmitForm.setStatus(ReservationStatus.SERVICE_COMPLETED); //서비스 완료

		Review serviceComplete = serviceCompleteRequestDTO.toEntity(reservationSubmitForm);
		try{
			reservationFormRepository.save(reservationSubmitForm);
			reviewRepository.save(serviceComplete);
		}catch(Exception e){
			throw new CommonException(ErrorCode.ERROR_SERVICE_COMPLETE);
		}

		saveImage(serviceComplete,serviceCompleteRequestDTO.getBefore(),ImageType.BEFORE);
		saveImage(serviceComplete,serviceCompleteRequestDTO.getAfter(),ImageType.AFTER);

		return true;
	}

	/**
	 * @apiNote: 서비스 완료 폼 제출 체크
	 */
	@Override
	@Transactional(readOnly = true)
	public ServiceCompleteResponseDTO checkServiceComplete(Long reservationId){
		Optional<ReservationSubmitForm> reservation = reservationFormRepository.serviceComplete(reservationId);
		nullCheck(reservation);

		if(reviewRepository.existReservation(reservationId)){
			throw new CommonException(ErrorCode.SUBMITTED_SERVICE_COMPLETE);
		}
		List<ReservationAnswer> answers =  reservation.get().getReservationAnswerList();
		Map<QuestionIdentify, String> answerMap
				= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

		ServiceCompleteResponseDTO completeResponseDTO =
				ServiceCompleteResponseDTO.builder()
				.serviceDate(answerMap.get(QuestionIdentify.SERVICEDATE))
				.serviceAddress(answerMap.get(QuestionIdentify.ADDRESS))
				.build();

		return completeResponseDTO;
	}

	/**
	 * @apiNote: 서비스 완료 폼 리스트 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ServiceCompleteInfoDTO> listServiceComplete() {
		List<ReservationSubmitForm> completeList = reservationFormRepository.serviceCompleteList();
		List<ServiceCompleteInfoDTO> dtoList = new ArrayList<>();
		for(ReservationSubmitForm submitForm : completeList){

			Member member = submitForm.getMember();
			List<ReservationAnswer> answers =  submitForm.getReservationAnswerList();
			Map<QuestionIdentify, String> answerMap
					= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));
			ServiceCompleteInfoDTO dto =new ServiceCompleteInfoDTO();
			dto.setAddress(answerMap.get(QuestionIdentify.ADDRESS));
			dto.setName(member.getUsername());
			dto.setCreatedAt(submitForm.getCreatedAt().toString());
			dto.setTotalPrice(submitForm.getTotalPrice());
			dto.setEstimate(answerMap.get(QuestionIdentify.SERVICEDURATION));
			dto.setPhoneNumber(answerMap.get(QuestionIdentify.APPLICANTCONACTINFO));
			dto.setReservationStatus(submitForm.getStatus());
			dto.setPayMentStatus(submitForm.getPayMentStatus());
			dto.setReservationDate(answerMap.get(QuestionIdentify.SERVICEDATE));
			dto.setReservationId(submitForm.getId());
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * @apiNote: 리뷰쓰기 전 서비스 완료 리스트에서 선택
	 */
	@Override
	@Transactional(readOnly = true)
	public ReviewAutoResponseDTO selectReviewData(Long reservationId) {
		Optional<Review> review = reviewRepository.serviceCompleteWithImages(reservationId);
		if(review.isEmpty()){
			throw new CommonException(ErrorCode.SUBMITTED_REVIEW_COMPLETE);
		}

		List<ReservationAnswer> answers =  review.get().getReservationSubmitForm().getReservationAnswerList();
		Map<QuestionIdentify, String> answerMap
				= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

		List<Image> images = review.get().getReviewImage();
		Map<Long, String> before = new HashMap<>();
		Map<Long, String> after = new HashMap<>();
		for(Image image : images){
			if(image.getImgType().equals(ImageType.BEFORE)){
				before.put(image.getId(),image.getImgUrl());
			}else{
				after.put(image.getId(),image.getImgUrl());
			}
		}
		ReviewAutoResponseDTO responseDTO = ReviewAutoResponseDTO.builder()
				.reservationId(review.get().getReservationSubmitForm().getId())
				.name(answerMap.get(QuestionIdentify.APPLICANTNAME))
				.address(answerMap.get(QuestionIdentify.ADDRESS))
				.reservationDate(answerMap.get(QuestionIdentify.SERVICEDATE))
				.before(before)
				.after(after)
				.build();

		return responseDTO;
	}


	/**
	 * @apiNote: Review 리스트 어드민
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReviewListAdminDTO> listReviewAdmin() {
		List<Review> reviews = reviewRepository.reviewList();
		List<ReviewListAdminDTO> reviewListAdminDTOS=new ArrayList<>();
		for(Review review:reviews){
			ReservationSubmitForm reservation = review.getReservationSubmitForm();
			List<ReservationAnswer> answers =  reservation.getReservationAnswerList();
			Map<QuestionIdentify, String> answerMap
					= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

			ReviewListAdminDTO reviewListAdminDTO = ReviewListAdminDTO.builder()
					.reviewId(review.getId())
					.name(answerMap.get(QuestionIdentify.APPLICANTNAME))
					.address(answerMap.get(QuestionIdentify.ADDRESS))
					.title(review.getTitle())
					.content(review.getContent())
					.createAt(review.getUpdatedAt().toString())
					.best(review.getBest())
					.build();
			reviewListAdminDTOS.add(reviewListAdminDTO);
		}
		return reviewListAdminDTOS;
	}

	/***
	 * @apiNote : 유저 리뷰리스트 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReviewListUserDTO> listReview() {
		List<Review> reviews = reviewRepository.reviewList();
		List<ReviewListUserDTO> reviewListUserDTOS=new ArrayList<>();
		for(Review review:reviews){
			ReservationSubmitForm reservation = review.getReservationSubmitForm();
			List<ReservationAnswer> answers =  reservation.getReservationAnswerList();
			Map<QuestionIdentify, String> answerMap
					= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));
			List<Image> images = review.getReviewImage();
			List<String>before = new ArrayList<>();
			List<String>after = new ArrayList<>();
			for(Image image : images){
				if(image.getImgType().equals(ImageType.BEFORE)){
					before.add(image.getImgUrl());
				}else{
					after.add(image.getImgUrl());
				}
			}
			ReviewListUserDTO reviewListUserDTO = ReviewListUserDTO.
					builder()
					.name(answerMap.get(QuestionIdentify.APPLICANTNAME))
					.address(answerMap.get(QuestionIdentify.ADDRESS))
					.date(answerMap.get(QuestionIdentify.SERVICEDATE))
					.title(review.getTitle())
					.content(review.getContent())
					.before(before)
					.after(after)
					.bestReview(review.getBest())
					.build();
			reviewListUserDTOS.add(reviewListUserDTO);
		}
		return reviewListUserDTOS;
	}

	@Override
	@Transactional(readOnly = true)
	public ReviewListUserDTO findBestReview(){
		Optional<Review> bestReview = reviewRepository.findByBestReviewUser();
		if(bestReview.isEmpty()){
			throw new CommonException(ErrorCode.NOT_FOUND_BEST_REVIEW);
		}
		ReservationSubmitForm reservation = bestReview.get().getReservationSubmitForm();
		List<ReservationAnswer> answers =  reservation.getReservationAnswerList();
		Map<QuestionIdentify, String> answerMap
				= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));
		List<Image> images = bestReview.get().getReviewImage();
		List<String>before = new ArrayList<>();
		List<String>after = new ArrayList<>();
		for(Image image : images){
			if(image.getImgType().equals(ImageType.BEFORE)){
				before.add(image.getImgUrl());
			}else{
				after.add(image.getImgUrl());
			}
		}
		ReviewListUserDTO bestReviewDTO = ReviewListUserDTO.
				builder()
				.name(answerMap.get(QuestionIdentify.APPLICANTNAME))
				.address(answerMap.get(QuestionIdentify.ADDRESS))
				.date(answerMap.get(QuestionIdentify.SERVICEDATE))
				.title(bestReview.get().getTitle())
				.content(bestReview.get().getContent())
				.before(before)
				.after(after)
				.bestReview(bestReview.get().getBest())
				.build();
		return bestReviewDTO;
	}

	/**
	 * @apiNote: 이미지 삭제
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean deleteReviewImage(Long imageId) {
		Optional<Image> image = imageRepository.findById(imageId);
		nullCheck(image);
		s3Service.deleteFile(image.get().getImgName());
		imageRepository.deleteById(imageId);
		return true;
	}

	/**
	 * @apiNote: 리뷰 삭제
	 */
	@Override
	public boolean deleteReview(Long reviewId) {
		Optional<Review> review = reviewRepository.findById(reviewId);
		nullCheck(review);

		Review deleteReview=review.get();
		deleteReview.reviewDelete();
		reviewRepository.save(deleteReview);
		return true;
	}

	/**
	 * @apiNote: 베스트 리뷰 저장
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean selectBestReview(Long reviewId) {
		Optional<Review> review=reviewRepository.findById(reviewId);
		nullCheck(review);

		Optional<Review> oldBestReview = reviewRepository.findByBestReview();
		if(oldBestReview.isPresent()){ //이미 베스트 리뷰가 있을경우 원래 베스트리뷰 내리기
			Review beforeBestReview = oldBestReview.get();
			beforeBestReview.setBestReview(false);
			try{
				reviewRepository.save(beforeBestReview);
			}catch (Exception e){
				throw new CommonException(ErrorCode.ERROR_BEST_REVIEW_COMPLETE);
			}
		}
		//새로운 베스트 리뷰 업데이트
		Review newBestReview = review.get();
		newBestReview.setBestReview(true);
		try{
			reviewRepository.save(newBestReview);
		}catch (Exception e){
			throw new CommonException(ErrorCode.ERROR_BEST_REVIEW_COMPLETE);
		}
		return true;
	}





	/***
	 * @apiNote : 리뷰 업로드
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public boolean uploadReview(Long reservationId, ReviewRequestDTO reviewRequestDTO) {
		Optional<Review> review = reviewRepository.findByReservationId(reservationId);
		nullCheck(review);
		Review uploadReview = review.get();
		uploadReview.reviewUpload(reviewRequestDTO);

		if(reviewRequestDTO.getBefore()!=null){
			saveImage(uploadReview,reviewRequestDTO.getBefore(),ImageType.BEFORE);
		}
		if(reviewRequestDTO.getAfter()!=null){
			saveImage(uploadReview,reviewRequestDTO.getAfter(),ImageType.AFTER);
		}

		try {
			reviewRepository.save(uploadReview);
		}catch (Exception e){
			throw new CommonException(ErrorCode.ERROR_REVIEW_COMPLETE);
		}
		return true;
	}


	/**
	 * @apiNote: null 체크
	 */
	private void nullCheck(Optional<?> data){
		if(data.isEmpty()){
			throw new CommonException(ErrorCode.NOT_FOUND_QUESTION);
		}
	}

	/**
	 * @apiNote: 이미지 저장
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	void saveImage(Review serviceComplete, List<MultipartFile> images, ImageType imageType) {
		List<String> imageNameList = s3Service.uploadFile(images, imageType.toString());
		List<String> imageUrlList = s3Service.uploadFileUrl(imageNameList, imageType.toString());

		List<Image> saveImages = IntStream.range(0, imageNameList.size())
				.mapToObj(i -> {
					String imgName = imageNameList.get(i);
					String imgUrl = imageUrlList.get(i);
					Image image = Image.builder()
							.imgName(imgName)
							.imgUrl(imgUrl)
							.review(serviceComplete)
							.imgType(imageType)
							.build();
					return image;
				})
				.collect(Collectors.toList());

		try{
			imageRepository.saveAll(saveImages);

		} catch(Exception e){
			throw new CommonException(ErrorCode.ERROR_IMAGE_COMPLETE);
		}

	}




}
