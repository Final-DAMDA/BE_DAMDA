package com.damda.back.repository;

import com.damda.back.domain.manager.AreaManager;
import com.damda.back.repository.custom.AreaManagerCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaManagerRepository extends JpaRepository<AreaManager, AreaManager.AreaManagerKey>, AreaManagerCustomRepository {
}
