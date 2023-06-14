package com.damda.back.service.Impl;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.response.MatchingAcceptGetDTO;
import com.damda.back.data.response.MatchingListDTO;
import com.damda.back.domain.Match;
import com.damda.back.domain.ReservationAnswer;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.area.Area;
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
	public void matchingListUp(ReservationSubmitForm reservationSubmitForm, String district) {
		List<AreaManager> areaManagerList = areaManagerRepository.findAreaManagerList(district);
		for (AreaManager areaManager : areaManagerList) {
			Match match = Match.builder()
					.managerId(areaManager.getAreaManagerKey().getManager().getId())
					.managerName(areaManager.getAreaManagerKey().getManager().getName())
					.manager(areaManager.getAreaManagerKey().getManager())
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
		if(managerName.isEmpty()){
			throw new CommonException(ErrorCode.ACTIVITY_MANAGER_NOT_FOUND);
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

	@Override
	@Transactional(readOnly = true)
	public List<MatchingListDTO> matchingList(Long reservationId) {
		List<Match> matchList = matchRepository.matchList(reservationId);
		List<MatchingListDTO> matchingListDTOS = new ArrayList<>();
		for(Match m: matchList){
			MatchingListDTO dto = new MatchingListDTO(m);
			//TODO: 매니저 활동지역 리스트 DTO에 추가

		}
		return null;
	}


}
