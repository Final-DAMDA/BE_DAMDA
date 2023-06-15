package com.damda.back.repository.custom;

import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;

import java.util.List;

public interface AreaManagerCustomRepository {

	List<AreaManager> findAreaManagerList(String district);

	public List<AreaManager> findAreaManagerListByManagerId(Long managerId);
	List<AreaManager> findAreaByManagerId(Long managerId);
}
