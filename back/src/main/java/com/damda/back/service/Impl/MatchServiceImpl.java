package com.damda.back.service.Impl;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.response.MatchingAcceptGetDTO;
import com.damda.back.data.response.MatchingListDTO;
import com.damda.back.domain.Match;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.*;
import com.damda.back.service.MatchService;
import lombok.RequiredArgsConstructor;
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

		MatchingAcceptGetDTO matchingAcceptGetDTO =
				MatchingAcceptGetDTO.builder()
				.serviceAddress(answerMap.get(QuestionIdentify.ADDRESS))
				.servings(answerMap.get(QuestionIdentify.AFEWSERVINGS))
				.serviceHours(answerMap.get(QuestionIdentify.SERVICEDURATION))
				.serviceDate(answerMap.get(QuestionIdentify.SERVICEDATE))
				.managerName(managerName)
				.reservationId(reservation.getId())
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

		Match match = matchRepository.matchFindByReservationAndMember(reservation.getId(),manager.getId())
				.orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_MATCH));

		if(matchResponseStatus==MatchResponseStatus.YES){ match.matchStatusYes(); }
		else if(matchResponseStatus==MatchResponseStatus.NO){ match.matchStatusNO(); }

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

		for(Match m: matchList){
			MatchingListDTO dto = new MatchingListDTO(m);
			List<AreaManager> aa= areaManagerRepository.findAreaByManagerId(m.getManagerId());
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
	public void matchingOrder(List<Long> matchIds) {
		if(matchIds.isEmpty()){
			throw new CommonException(ErrorCode.NOT_FOUND_MATCH_ID);
		}
		for(Long matchId:matchIds){
			Match match = matchRepository.findById(matchId).orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_MATCH));
			match.matchingOrder();
		}
	}

}
