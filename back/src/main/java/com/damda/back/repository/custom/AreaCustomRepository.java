package com.damda.back.repository.custom;

import com.damda.back.domain.area.Area;

import java.util.List;
import java.util.Optional;

public interface AreaCustomRepository {
	Optional<Area> searchArea(String city, String district);

	public List<Area> searchActivityArea();

	Optional<Area> searchArea(String district);
}
