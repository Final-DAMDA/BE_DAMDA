package com.damda.back.repository;

import com.damda.back.domain.Match;
import com.damda.back.repository.custom.MatchCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match,Long>, MatchCustomRepository {
}
