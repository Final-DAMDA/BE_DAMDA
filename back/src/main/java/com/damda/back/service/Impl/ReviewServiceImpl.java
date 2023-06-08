package com.damda.back.service.Impl;

import com.damda.back.config.annotation.TimeChecking;
import com.damda.back.data.common.ImageType;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.data.response.ReviewAutoResponseDTO;
import com.damda.back.data.response.ServiceCompleteInfoDTO;
import com.damda.back.domain.*;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.ImageRepository;
import com.damda.back.repository.ReservationFormRepository;
import com.damda.back.repository.ReviewRepository;
import com.damda.back.repository.ServiceCompleteRepository;
import com.damda.back.service.ReviewService;
import com.damda.back.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ImageRepository imageRepository;
	private final S3Service s3Service;
	private final ReservationFormRepository reservationFormRepository;
	private final ReviewRepository reviewRepository;


	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public boolean uploadServiceComplete(Long reservationId, ServiceCompleteRequestDTO serviceCompleteRequestDTO){

		ReservationSubmitForm reservationSubmitForm = checkServiceComplete(reservationId);
		reservationSubmitForm.setStatus(ReservationStatus.SERVICE_COMPLETED); //서비스 완료

		Review serviceComplete = serviceCompleteRequestDTO.toEntity(reservationSubmitForm);
		try{
			reservationFormRepository.save(reservationSubmitForm);
			reviewRepository.save(serviceComplete);
		}catch(Exception e){
			throw new CommonException(ErrorCode.ERROR_SERVICE_COMPLETE);
		}

		saveImage(serviceComplete,serviceCompleteRequestDTO.getBefore(),serviceCompleteRequestDTO.getAfter());

		return true;
	}

	@Override
	public ReservationSubmitForm checkServiceComplete(Long reservationId){
		Optional<ReservationSubmitForm> reservation = reservationFormRepository.findById(reservationId);
		if(reservation.isEmpty()){
			throw new CommonException(ErrorCode.NOT_FOUND_QUESTION);
		}
		if(reviewRepository.existReservation(reservationId)){
			throw new CommonException(ErrorCode.SUBMITTED_SERVICE_COMPLETE);
		}
		return reservation.get();
	}

	@Override
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

	@Override
	public ReviewAutoResponseDTO selectReviewData(Long reservationId) {
		Optional<ReservationSubmitForm> reservation = reservationFormRepository.serviceComplete(reservationId);
		if(reservation.isEmpty()){
			throw new CommonException(ErrorCode.NOT_FOUND_QUESTION);
		}
		List<ReservationAnswer> answers =  reservation.get().getReservationAnswerList();
		Map<QuestionIdentify, String> answerMap
				= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));

		ReviewAutoResponseDTO responseDTO = ReviewAutoResponseDTO.builder()
											.reservationId(reservation.get().getId())
											.name(reservation.get().getMember().getUsername())
											.address(answerMap.get(QuestionIdentify.ADDRESS))
											.reservationDate(answerMap.get(QuestionIdentify.SERVICEDATE))
											.build();
		return responseDTO;
	}


	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public void saveImage(Review serviceComplete, List<MultipartFile> before, List<MultipartFile> after) {
		List<String> beforeNameList = s3Service.uploadFile(before, ImageType.BEFORE.toString());
		List<String> beforeUrlList = s3Service.uploadFileUrl(beforeNameList, ImageType.BEFORE.toString());
		List<String> afterNameList = s3Service.uploadFile(after, ImageType.AFTER.toString());
		List<String> afterUrlList = s3Service.uploadFileUrl(afterNameList, ImageType.AFTER.toString());

		List<Image> beforeImages = IntStream.range(0, beforeNameList.size())
				.mapToObj(i -> {
					String imgName = beforeNameList.get(i);
					String imgUrl = beforeUrlList.get(i);
					Image image = Image.builder()
							.imgName(imgName)
							.imgUrl(imgUrl)
							.review(serviceComplete)
							.imgType(ImageType.BEFORE)
							.build();
					return image;
				})
				.collect(Collectors.toList());

		List<Image> afterImages = IntStream.range(0, afterNameList.size())
				.mapToObj(i -> {
					String imgName = afterNameList.get(i);
					String imgUrl = afterUrlList.get(i);
					Image image = Image.builder()
							.imgName(imgName)
							.imgUrl(imgUrl)
							.review(serviceComplete)
							.imgType(ImageType.AFTER)
							.build();
					return image;
				})
				.collect(Collectors.toList());

		try{
			imageRepository.saveAll(beforeImages);
			imageRepository.saveAll(afterImages);
		} catch(Exception e){
			throw new CommonException(ErrorCode.ERROR_IMAGE_COMPLETE);
		}

	}


}
