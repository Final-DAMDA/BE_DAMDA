package com.damda.back.repository;

import com.damda.back.domain.area.Area;
import com.damda.back.repository.custom.AreaCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long>, AreaCustomRepository {
}
