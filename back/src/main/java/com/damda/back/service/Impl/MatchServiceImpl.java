package com.damda.back.service.Impl;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.common.ReservationStatus;
import com.damda.back.data.request.MatchingFailToManagerDTO;
import com.damda.back.data.request.MatchingSuccessToManagerDTO;
import com.damda.back.data.request.MatchingSuccessToUserDTO;
import com.damda.back.data.response.*;
import com.damda.back.domain.Match;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.*;
import com.damda.back.service.MatchService;
import com.damda.back.service.TalkSendService;
import com.damda.back.utils.SolapiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
	private final AreaRepository areaRepository;
	private final AreaManagerRepository areaManagerRepository;
	private final ManagerRepository managerRepository;
	private final MatchRepository matchRepository;
	private final ReservationFormRepository reservationFormRepository;
	private final SolapiUtils solapiUtils;
	private final TalkSendService talkSendService;


	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void matchingListUp(ReservationSubmitForm reservationSubmitForm, List<Manager> managerList) {
		for (Manager manager : managerList) {
			Match match = Match.builder()
					.matching(false)
					.managerId(manager.getId())
					.managerName(manager.getManagerName())
					.manager(manager)
					.matchStatus(MatchResponseStatus.WAITING)
					.reservationForm(reservationSubmitForm)
					.build();
			try {
				matchRepository.save(match);
			} catch (Exception e) {
				throw new CommonException(ErrorCode.ERROR_MATCH_COMPLETE);
			}

		}
	}

	/**
	 * @apiNote : matching 수락폼 GET
	 */
	@Transactional(readOnly = true)
	@Override
	public MatchingAcceptGetDTO matchingAcceptInfo(Long reservationId, Integer memberId) {
		ReservationSubmitForm reservation = reservationFormRepository.findByreservationId(reservationId)
				.orElseThrow(()->new CommonException(ErrorCode.FORM_NOT_FOUND));

		String managerName = managerRepository.findManagerName(memberId);
		if(managerName==null){
			throw new CommonException(ErrorCode.NOT_FOUND_LOGIN_MANAGER);
		}

		List<ReservationAnswer> answers =  reservation.getReservationAnswerList();
		Map<QuestionIdentify, String> answerMap
				= answers.stream().collect(Collectors.toMap(ReservationAnswer::getQuestionIdentify, ReservationAnswer::getAnswer));
		String date = answerMap.get(QuestionIdentify.SERVICEDATE);
		String dateString = date.substring(0, 10); // yyyy-mm-dd 추출
		String timeString = date.substring(11,16); // hh:mm 추출

		ServiceInfo serviceInfo = ServiceInfo.builder()
				.serviceDate(dateString)
				.serviceDuration(answerMap.get(QuestionIdentify.SERVICEDURATION))
				.servicePerPerson(reservation.getServicePerson().toString()+"인")
				.location(answerMap.get(QuestionIdentify.ADDRESS))
				.build();
		ReservationInfo reservationInfo = ReservationInfo.builder().parkAvailable(answerMap.get(QuestionIdentify.PARKINGAVAILABLE))
				.reservationEnter(answerMap.get(QuestionIdentify.RESERVATIONENTER))
				.reservationNote(answerMap.get(QuestionIdentify.RESERVATIONNOTE))
				.reservationRequest(answerMap.get(QuestionIdentify.RESERVATIONREQUEST))
				.build();
		MatchingAcceptGetDTO matchingAcceptGetDTO =
				MatchingAcceptGetDTO.builder()
				.manager(managerName)
				.id(reservation.getId())
				.serviceInfo(serviceInfo)
				.reservationInfo(reservationInfo)
				.build();
		return matchingAcceptGetDTO;
	}

	/**
	 * @apiNote : 매칭 수락폼 POST
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void matchingAccept(Long reservationId, Integer memberId, MatchResponseStatus matchResponseStatus) {
		ReservationSubmitForm reservation = reservationFormRepository.findByreservationId(reservationId)
				.orElseThrow(()->new CommonException(ErrorCode.FORM_NOT_FOUND));

		Manager manager = managerRepository.findManager(memberId).
				orElseThrow(()->new CommonException(ErrorCode.ACTIVITY_MANAGER_NOT_FOUND));
		//TODO: 굳이 객체 전부 안가져와도 되긴 함, 나중에 id만 가져오는 걸로 바꾸기
		System.out.println(manager);
		Match match = matchRepository.matchFindByReservationAndMember(reservation.getId(),manager.getId())
				.orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_MATCH));

		if(matchResponseStatus==MatchResponseStatus.YES){
			match.matchStatusYes();
			reservation.setMatchingWaiting();
		}
		else if(matchResponseStatus==MatchResponseStatus.NO){
			match.matchStatusNO();
		}

		try{
			matchRepository.save(match);
		}catch (Exception e){
			throw new CommonException(ErrorCode.ERROR_MATCH_COMPLETE);
		}

	}

	/**
	 * @apiNote: 매칭리스트(현황)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MatchingListDTO> matchingList(Long reservationId) {
		List<Match> matchList = matchRepository.matchList(reservationId);
		List<MatchingListDTO> matchingListDTOS = new ArrayList<>();
		List<Manager> collect = matchList.stream().map((match -> match.getManager())).collect(Collectors.toList());
		List<AreaManager> areaManagerList = collect.stream().flatMap(manager -> manager.getAreaManagers().stream()).collect(Collectors.toList());
		managerRepository.areaList2(areaManagerList);

		for(Match m: matchList){
			MatchingListDTO dto = new MatchingListDTO(m);
			List<AreaManager> aa= m.getManager().getAreaManagers();
			List<String> managerActivity = new ArrayList<>();
			for(AreaManager a: aa){
				managerActivity.add(a.getAreaManagerKey().getArea().getDistrict());
			}
			dto.setActivityArea(managerActivity);

			matchingListDTOS.add(dto);
		}
		return matchingListDTOS;
	}

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void matchingOrder(Long reservationId, List<Long> matchIds) {
		if(matchIds.isEmpty()){
			throw new CommonException(ErrorCode.NOT_FOUND_MATCH_ID);
		}
		for(Long matchId:matchIds){
			Match match = matchRepository.matchFindByReservationAndMatch(reservationId,matchId)
					.orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_MATCH));
			match.matchingOrder();
		} //TODO: 해당 매치에서 예약 ID 가져와서 매칭 실패된 매니저와 매칭성공 매니저, 유저한테 알림톡 보내야 함 , 예약 상태 바꾸기 -> 신청인원만큼 수락되었으면 바로 예약 확정, 아니면 매칭대기
		//해당 예약 상태값 바꾸기,
		List<Match> matches = matchRepository.matchList(reservationId);
		ReservationSubmitForm reservationSubmitForm = reservationFormRepository.findByreservationId(reservationId).orElseThrow(
				()->new CommonException(ErrorCode.NOT_FOUND_RESERIVATION));
		if(matches.isEmpty()){
			throw new CommonException(ErrorCode.NOT_FOUND_MATCH);
		}

		int managerCount = 0;
		for(Match match:matches){
			if(match.isMatching()){
				managerCount++;
			}
		}
		if(managerCount!=0 && managerCount<reservationSubmitForm.getServicePerson()){
			reservationSubmitForm.changeStatus(ReservationStatus.WAITING_FOR_ACCEPT_MATCHING);
		}
		if(managerCount==reservationSubmitForm.getServicePerson()){
			reservationSubmitForm.changeStatus(ReservationStatus.MANAGER_MATCHING_COMPLETED);
			talkSendService.sendReservationCompleted(matches,reservationSubmitForm);
		}
		if(managerCount>reservationSubmitForm.getServicePerson()){
			throw new CommonException(ErrorCode.OVER_MATCH_ORDER);
		}

	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public PageReservationManagerIdDTO reservationListManagerDTO(Long managerId, Pageable pageable) {
		Manager manager= managerRepository.findById(managerId).orElseThrow
				(()-> new CommonException(ErrorCode.NOT_FOUND_MANAGER));

		Page<ReservationSubmitForm> reservationSubmitForms = matchRepository.findByManagerId(managerId,pageable);

		List<ReservationListManagerIDDTO> dtos = new ArrayList<>();
		for(ReservationSubmitForm form:reservationSubmitForms){
			ReservationListManagerIDDTO dto = ReservationListManagerIDDTO.builder()
					.id(form.getId())
					.createdAt(form.getReservationDate())
					.build();
			dtos.add(dto);
		}
		PageReservationManagerIdDTO pageReservationManagerIdDTO = PageReservationManagerIdDTO
																	.builder().content(dtos)
																	.first(reservationSubmitForms.isFirst())
																	.last(reservationSubmitForms.isLast())
																	.total(reservationSubmitForms.getTotalElements())
																	.build();
		return pageReservationManagerIdDTO;
	}

}
