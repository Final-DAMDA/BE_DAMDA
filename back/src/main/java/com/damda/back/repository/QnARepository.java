package com.damda.back.repository;

import com.damda.back.domain.QnA;
import com.damda.back.repository.custom.QnACustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long>, QnACustomRepository {
}
