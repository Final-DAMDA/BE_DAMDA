package com.damda.back.repository;

import com.damda.back.domain.GroupIdCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupIdCodeRepository extends JpaRepository<GroupIdCode,Long> {
}
