package com.damda.back.repository;

import com.damda.back.domain.Question;
import com.damda.back.repository.custom.QuestionCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long>, QuestionCustomRepository {
}
