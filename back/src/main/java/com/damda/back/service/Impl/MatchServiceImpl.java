package com.damda.back.service.Impl;

import com.damda.back.domain.manager.Manager;
import com.damda.back.repository.AreaManagerRepository;
import com.damda.back.repository.AreaRepository;
import com.damda.back.repository.ManagerRepository;
import com.damda.back.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
	private final AreaRepository areaRepository;
	private final AreaManagerRepository areaManagerRepository;
	private final ManagerRepository managerRepository;


	@Override
	public void matchingListUp(String district) {

	}
}
