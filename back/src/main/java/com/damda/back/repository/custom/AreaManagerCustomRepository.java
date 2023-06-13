package com.damda.back.repository.custom;

import com.damda.back.domain.manager.AreaManager;

import java.util.List;

public interface AreaManagerCustomRepository {

	List<AreaManager> findAreaManagerList(Long areaId);
}
