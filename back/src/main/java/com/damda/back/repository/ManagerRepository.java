package com.damda.back.repository;

import com.damda.back.domain.manager.Manager;
import com.damda.back.repository.custom.ManagerCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerCustomRepository {
}
