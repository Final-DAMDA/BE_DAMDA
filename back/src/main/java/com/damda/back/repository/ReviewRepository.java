package com.damda.back.repository;

import com.damda.back.domain.Review;
import com.damda.back.repository.custom.ReviewCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>, ReviewCustomRepository {
}
