package com.damda.back.repository;

import com.damda.back.domain.Member;
import com.damda.back.repository.custom.MemberCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Integer> , MemberCustomRepository {
}
