package com.damda.back.repository;

import com.damda.back.domain.ReservationSubmitForm;
import com.damda.back.repository.custom.ReservationFormCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationFormRepository extends JpaRepository<ReservationSubmitForm,Long>, ReservationFormCustomRepository {
}
