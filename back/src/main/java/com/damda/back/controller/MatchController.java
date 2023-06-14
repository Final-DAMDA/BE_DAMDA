package com.damda.back.controller;

import com.damda.back.data.common.CodeEnum;
import com.damda.back.data.common.CommonResponse;
import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.response.MemberResponseDTO;
import com.damda.back.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MatchController {
	private final MatchService matchService;

	/**
	 * @apiNote : 매칭 수락 폼 - 매니저한테 예약 수락/거절 GET정보 보내주기
	 * @param request
	 * @param reservationId
	 * @return
	 */
	@GetMapping("/api/v1")
	public ResponseEntity<CommonResponse<?>> matchingInfoGET(HttpServletRequest request, @RequestParam Long reservationId){//reservationID 임 PathVariable로 바꿔도 될

		Integer memberId =  Integer.parseInt(request.getAttribute("id").toString());


		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data(matchService.matchingAcceptInfo(reservationId,memberId))
				.build();
		return ResponseEntity.ok(commonResponse);
	}

	/**
	 * @apiNote : 매칭 수락 폼 - 매니저한테 예약 수락/거절 POST
	 * @param request
	 * @param reservationId
	 * @return
	 */
	@PostMapping("/api/v1") //status=YES OR NO
	public ResponseEntity<CommonResponse<?>> matchingAccept(HttpServletRequest request, @RequestParam Long reservationId, @RequestParam String status){//reservationID 임

		Integer memberId =  Integer.parseInt(request.getAttribute("id").toString());
		matchService.matchingAccept(reservationId,memberId, MatchResponseStatus.valueOf(status));
		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data("")
				.build();
		return ResponseEntity.ok(commonResponse);
	}

	@GetMapping("/api/v1/matching/list/{id}")
	public ResponseEntity<CommonResponse<?>> matchingAccept( @PathVariable("id") Long reservationId){//reservationID 임

		CommonResponse<?> commonResponse = CommonResponse
				.builder()
				.codeEnum(CodeEnum.SUCCESS)
				.data("")
				.build();
		return ResponseEntity.ok(commonResponse);
	}


}