package com.damda.back.service.Impl;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.data.common.QuestionIdentify;
import com.damda.back.data.response.MatchingAcceptGetDTO;
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
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public void matchingListUp(ReservationSubmitForm reservationSubmitForm, String district) {
		List<AreaManager> areaManagerList = areaManagerRepository.findAreaManagerList(district);
		for (AreaManager areaManager : areaManagerList) {
			Match match = Match.builder()
					.managerId(areaManager.getAreaManagerKey().getManager().getId())
					.managerName(areaManager.getAreaManagerKey().getManager().getManagerName())
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
	 * @param reservationId
	 * @param memberId
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public MatchingAcceptGetDTO matchingAcceptInfo(Long reservationId, Integer memberId) {
		ReservationSubmitForm reservation = reservationFormRepository.findByreservationId(reservationId).orElseThrow(()->new CommonException(ErrorCode.FORM_NOT_FOUND));
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
}
