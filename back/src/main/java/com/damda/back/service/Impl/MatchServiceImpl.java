package com.damda.back.service.Impl;

import com.damda.back.data.common.MatchResponseStatus;
import com.damda.back.domain.Match;
import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import com.damda.back.exception.CommonException;
import com.damda.back.exception.ErrorCode;
import com.damda.back.repository.AreaManagerRepository;
import com.damda.back.repository.AreaRepository;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.repository.MatchRepository;
import com.damda.back.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
	private final AreaRepository areaRepository;
	private final AreaManagerRepository areaManagerRepository;
	private final ManagerRepository managerRepository;
	private final MatchRepository matchRepository;


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
}
