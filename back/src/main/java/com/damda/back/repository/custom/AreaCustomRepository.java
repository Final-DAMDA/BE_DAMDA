package com.damda.back.repository.custom;

import com.damda.back.domain.area.Area;

import java.util.Optional;

public interface AreaCustomRepository {
	Optional<Area> searchArea(String city, String district);
}
