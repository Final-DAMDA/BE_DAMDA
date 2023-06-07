package com.damda.back.service.Impl;

import com.damda.back.config.annotation.TimeChecking;
import com.damda.back.data.common.ImageType;
import com.damda.back.data.request.ServiceCompleteRequestDTO;
import com.damda.back.domain.Image;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.Review;
import com.damda.back.domain.ServiceComplete;
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

import java.util.List;
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
		Optional<ReservationSubmitForm> reservation = reservationFormRepository.findById(reservationId);
		if(reservation.isEmpty()){
			throw new CommonException(ErrorCode.BAD_REQUEST,"해당 예약이 없습니다.");
		}
		if(reviewRepository.existReservation(reservationId)){
			throw new CommonException(ErrorCode.BAD_REQUEST,"이미 제출된 서비스 입니다.");
		}
		Review serviceComplete = serviceCompleteRequestDTO.toEntity(reservation.get());
		reviewRepository.save(serviceComplete);
		saveImage(serviceComplete,serviceCompleteRequestDTO.getBefore(),serviceCompleteRequestDTO.getAfter());

		return true;
	}


	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public void saveImage(Review serviceComplete, List<MultipartFile> before, List<MultipartFile> after) {
		List<String> beforeNameList = s3Service.uploadFile(before, ImageType.BEFORE.toString());
		List<String> beforeUrlList = s3Service.uploadFileUrl(beforeNameList);
		List<String> afterNameList = s3Service.uploadFile(after, ImageType.AFTER.toString());
		List<String> afterUrlList = s3Service.uploadFileUrl(afterNameList);

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

		imageRepository.saveAll(beforeImages);
		imageRepository.saveAll(afterImages);
	}


}
