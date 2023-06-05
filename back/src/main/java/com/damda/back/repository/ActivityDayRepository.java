package com.damda.back.repository;

import com.damda.back.domain.manager.ActivityDay;
import com.damda.back.repository.custom.ActivityDayCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityDayRepository extends JpaRepository<ActivityDay, Long>, ActivityDayCustomRepository {
}
