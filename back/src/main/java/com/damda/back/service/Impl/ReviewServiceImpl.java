package com.damda.back.service.Impl;

import com.damda.back.data.common.ImageType;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.request.ReviewManualRequestDTO;
import com.damda.back.data.request.ReviewRequestDTO;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.data.response.*;
import com.damda.back.domain.*;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.*;
import com.damda.back.service.ReviewService;
import com.damda.back.service.S3Service;
import com.damda.back.service.TalkSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	private final MatchRepository matchRepository;
	private final ReservationAnswerRepository reservationAnswerRepository;
	private final TalkSendService talkSendService;



	/**
	 * @apiNote: 서비스 완료 폼 업로드
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public boolean uploadServiceComplete(Long reservationId, ServiceCompleteRequestDTO serviceCompleteRequestDTO){
		ReservationSubmitForm reservationSubmitForm = reservationFormRepository.findByreservationId(reservationId)
				.orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_RESERIVATION));

		if(reviewRepository.existReservation(reservationId)){
			throw new CommonException(ErrorCode.SUBMITTED_SERVICE_COMPLETE);
		}
		reservationSubmitForm.statusServiceComplete(); //서비스 완료

		Review serviceComplete = serviceCompleteRequestDTO.toEntity(reservationSubmitForm);

		try{
			reservationFormRepository.save(reservationSubmitForm);
			reviewRepository.save(serviceComplete);
		}catch(Exception e){
			throw new CommonException(ErrorCode.ERROR_SERVICE_COMPLETE);
		}

		saveImage(serviceComplete,serviceCompleteRequestDTO.getBefore(),ImageType.BEFORE);
		saveImage(serviceComplete,serviceCompleteRequestDTO.getAfter(),ImageType.AFTER);

		//TODO: 유저에게 서비스 완료 알림톡 보내기
		talkSendService.sendServiceCompletedUser(reservationSubmitForm);
		return true;
	}

	/**
	 * @apiNote: 서비스 완료 폼 제출 체크
	 */
	@Override
	@Transactional(readOnly = true)
	public ServiceCompleteResponseDTO checkServiceComplete(Long reservationId){
		ReservationSubmitForm reservation = reservationFormRepository.findByreservationId(reservationId).orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_RESERIVATION));
		if(reviewRepository.existReservation(reservationId)){
			throw new CommonException(ErrorCode.SUBMITTED_SERVICE_COMPLETE);
		}

		List<ReservationAnswer> answers =  reservation.getReservationAnswerList();
		Map<QuestionIdentify, String> answerMap
				= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));
		List<String> managerName= matchRepository.matchListFindManager(reservation.getId());


		ServiceCompleteResponseDTO completeResponseDTO =
						ServiceCompleteResponseDTO.builder()
						.serviceDate(answerMap.get(QuestionIdentify.SERVICEDATE))
						.serviceUsageTime(answerMap.get(QuestionIdentify.SERVICEDURATION))
						.managerCount(reservation.getServicePerson())
						.serviceAddress(answerMap.get(QuestionIdentify.ADDRESS))
						.reservationId(reservation.getId())
						.managerNames(managerName)
						.build();

		return completeResponseDTO;
	}

	/**
	 * @apiNote: 서비스 완료 폼 리스트 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<ServiceCompleteInfoDTO> listServiceComplete(Pageable pageable) {
		Page<ReservationSubmitForm> completeList = reservationFormRepository.serviceCompleteList(pageable);

		List<ServiceCompleteInfoDTO> dtoList = completeList.stream().map(submitForm -> {
			Member member = submitForm.getMember();
			List<ReservationAnswer> answers = submitForm.getReservationAnswerList();
			Map<QuestionIdentify, String> answerMap = answers.stream()
					.collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

			List<String>managerNames = submitForm.getMatches().stream()
					.filter(match -> match.isMatching()).map(Match::getManagerName).collect(Collectors.toList());

			ServiceCompleteInfoDTO dto = new ServiceCompleteInfoDTO();
			dto.setAddress(answerMap.get(QuestionIdentify.ADDRESS));
			dto.setName(member.getUsername());
			dto.setTotalPrice(submitForm.getTotalPrice());
			dto.setPhoneNumber(answerMap.get(QuestionIdentify.APPLICANTCONACTINFO));
			dto.setReservationDate(answerMap.get(QuestionIdentify.SERVICEDATE));
			dto.setReservationId(submitForm.getId());
			dto.setManagerNames(managerNames);
			return dto;
		}).collect(Collectors.toList());

		Page<ServiceCompleteInfoDTO> resultPage = new PageImpl<>(dtoList, pageable, completeList.getTotalElements());
		return resultPage;
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
		List<ImageResponseDTO> before = new ArrayList<>();
		List<ImageResponseDTO> after = new ArrayList<>();
		for(Image image : images){
			ImageResponseDTO dto = new ImageResponseDTO().builder()
									.id(image.getId())
									.imgUrl(image.getImgUrl())
									.build();
			if(image.getImgType().equals(ImageType.BEFORE)){
				before.add(dto);
			}else{
				after.add(dto);
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
	public Page<ReviewListAdminDTO> listReviewAdmin(Pageable pageable) {
		Page<Review> reviews = reviewRepository.reviewList(pageable);
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
		Page<ReviewListAdminDTO> resultPage = new PageImpl<>(reviewListAdminDTOS, pageable, reviews.getTotalElements());
		return resultPage;
	}

	@Override
	@Transactional(readOnly = true)
	public BeforeAfterImageDTO serviceCompleteDetail(Long reservationId) {
		Review review =reviewRepository.serviceCompleteFindByReservation(reservationId)
				.orElseThrow(()-> new CommonException(ErrorCode.NOT_FOUND_SERVICE_COMPLETE));

		List<Image> images = review.getReviewImage();
		List<String> before = new ArrayList<>();
		List<String> after = new ArrayList<>();
		for(Image image : images){
			if(image.getImgType().equals(ImageType.BEFORE)){
				before.add(image.getImgUrl());
			}else{
				after.add(image.getImgUrl());
			}
		}
		BeforeAfterImageDTO beforeAfterImageDTO = BeforeAfterImageDTO.builder()
												.before(before)
												.after(after)
												.build();
		return beforeAfterImageDTO;
	}


	/***
	 * @apiNote : 유저 리뷰리스트 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ReviewListUserDTO> listReview() {
		List<Review> reviews = reviewRepository.reviewListUser();
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
					.date(review.getUpdatedAt().toString())
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
		Image image = imageRepository.findById(imageId)
				.orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_IMAGE));

		s3Service.deleteFile(image.getImgName());
		imageRepository.deleteById(imageId);
		return true;
	}

	/**
	 * @apiNote: 리뷰 삭제
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_REVIEW));
		review.reviewDelete();
		return true;
	}

	/**
	 * @apiNote: 베스트 리뷰 저장
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean selectBestReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_REVIEW));

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
		Review newBestReview = review;
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
		Review uploadReview = reviewRepository.findByReservationId(reservationId).orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_REVIEW));
		uploadReview.reviewUpload(reviewRequestDTO);
		System.out.println(reviewRequestDTO);
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

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public Long manualReviewUpload(ReviewManualRequestDTO dto) {
		ReservationSubmitForm reservationSubmitForm = ReservationSubmitForm.builder()
				.deleted(true)
				.build();
		reservationFormRepository.save(reservationSubmitForm);

		ReservationAnswer name = ReservationAnswer.builder()
				.answer(dto.getName())
				.questionIdentify(QuestionIdentify.APPLICANTNAME)
				.reservationSubmitForm(reservationSubmitForm)
				.build();
		reservationAnswerRepository.save(name);

		ReservationAnswer address = ReservationAnswer.builder()
				.answer(dto.getAddress())
				.questionIdentify(QuestionIdentify.ADDRESS)
				.reservationSubmitForm(reservationSubmitForm)
				.build();
		reservationAnswerRepository.save(address);

		ReservationAnswer serviceDate = ReservationAnswer.builder()
				.answer(dto.getServiceDate())
				.questionIdentify(QuestionIdentify.SERVICEDATE)
				.reservationSubmitForm(reservationSubmitForm)
				.build();
		reservationAnswerRepository.save(serviceDate);

		Review review = Review.builder()
				.status(true)
				.best(false)
				.content(dto.getContent())
				.title(dto.getTitle())
				.reservationSubmitForm(reservationSubmitForm)
				.build();
		reviewRepository.save(review);
		saveImage(review,dto.getBefore(),ImageType.BEFORE);
		saveImage(review,dto.getAfter(),ImageType.AFTER);
		return review.getId();
	}




}
