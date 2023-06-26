package com.damda.back.repository.custom;

import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;

import java.util.List;
import java.util.Optional;

public interface AreaManagerCustomRepository {

	List<AreaManager> findAreaManagerList(String district);

	List<AreaManager> findAreaManagerListByManagerId(Long managerId);
	List<AreaManager> findAreaByManagerId(Long managerId);
	Optional<AreaManager> findAreaByManagerId(Long managerId, String city, String district);
	List<AreaManager> findAreaByManagerId2(Long managerId);
}
